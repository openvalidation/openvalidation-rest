/*
 *    Copyright 2019 BROCKHAUS AG
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.openvalidation.rest.model.dto;

import io.openvalidation.common.ast.*;
import io.openvalidation.common.exceptions.ASTValidationException;
import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.common.model.OpenValidationResult;
import io.openvalidation.common.utils.Constants;
import io.openvalidation.core.Aliases;
import io.openvalidation.rest.service.OVParams;
import io.openvalidation.rest.service.OpenValidationService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UnkownElementParser {
  private ASTModel astModel;
  private OVParams parameter;

  public UnkownElementParser(ASTModel astModel, OVParams parameter) {
    if (astModel == null) astModel = new ASTModel();
    if (astModel.getElements().size() == 0 && parameter != null)
      astModel.add(new ASTUnknown(parameter.getRule()));

    this.astModel = astModel;
    this.parameter = parameter;
  }

  public List<ASTItem> generate(OpenValidationService ovService) throws Exception {
    if (astModel == null) return new ArrayList<>();

    List<ASTItem> astItemList =
        astModel.getElements().stream()
            .map(element -> (ASTItem) element)
            .collect(Collectors.toList());
    Map<Integer, ASTUnknown> unkownIdMap = new HashMap<>();

    for (ASTUnknown element : astModel.getUnknownElements()) {
      unkownIdMap.put(astModel.getElements().indexOf(element), element);
    }

    if (unkownIdMap.size() == 0) return astItemList;

    List<String> potentialKeywords =
        Aliases.getAliasByToken(this.parameter.getCulture(), Constants.AS_TOKEN);
    String asKeyword = potentialKeywords.size() > 0 ? potentialKeywords.get(0) : null;
    if (asKeyword == null) {
      throw new Exception("No Alias for " + Constants.AS_TOKEN + " found!");
    }

    List<String> variableSources =
        astModel.getVariables().stream()
            .map(ASTItem::getOriginalSource)
            .collect(Collectors.toList());
    String variableName =
        variableSources.size() > 0 ? String.join("\n\n", variableSources).concat("\n\n") : "";
    StringBuilder parsingStringBuilder = new StringBuilder(variableName);

    for (Map.Entry<Integer, ASTUnknown> entry : unkownIdMap.entrySet()) {
      parsingStringBuilder
          .append(entry.getValue().getOriginalSource())
          .append(" ")
          .append(asKeyword)
          .append(" ")
          .append("tmp_parsing " + entry.getKey())
          .append("\n\n");
    }

    OVParams newParameter =
        new OVParams(
            parsingStringBuilder.toString(),
            parameter.getSchema(),
            parameter.getCulture(),
            parameter.getLanguage());

    OpenValidationResult tmpResult = ovService.generate(newParameter);
    if (tmpResult.getASTModel() == null) return astItemList;

    List<ASTGlobalElement> elementList = tmpResult.getASTModel().getElements();
    if (elementList.size() == 0) return astItemList;

    List<ASTGlobalElement> relevantList =
        elementList.subList(elementList.size() - unkownIdMap.size(), elementList.size());

    int index = 0;
    int errorIndex = 0;
    for (Map.Entry<Integer, ASTUnknown> entry : unkownIdMap.entrySet()) {
      ASTItem relevantItem =
          relevantList.get(index) instanceof ASTVariable
              ? ((ASTVariable) relevantList.get(index)).getValue()
              : relevantList.get(index);
      if (relevantItem instanceof ASTUnknown) {
        if (tmpResult.getErrors().size() > errorIndex) {
          OpenValidationException exception = tmpResult.getErrors().get(errorIndex);
          if (exception instanceof ASTValidationException) {
            astItemList.set(entry.getKey(), ((ASTValidationException) exception).getItem());
          }
        }
        errorIndex++;
      } else {
        astItemList.set(entry.getKey(), relevantItem);
      }
      index++;
    }

    return astItemList;
  }
}
