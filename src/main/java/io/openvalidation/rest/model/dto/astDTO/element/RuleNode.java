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

package io.openvalidation.rest.model.dto.astDTO.element;

import io.openvalidation.common.ast.ASTActionError;
import io.openvalidation.common.ast.ASTRule;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.utils.Constants;
import io.openvalidation.core.Aliases;
import io.openvalidation.rest.model.dto.astDTO.GenericNode;
import io.openvalidation.rest.model.dto.astDTO.TransformationParameter;
import io.openvalidation.rest.model.dto.astDTO.operation.ConditionNode;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import io.openvalidation.rest.model.dto.astDTO.transformation.NodeGenerator;
import io.openvalidation.rest.model.dto.astDTO.transformation.RangeGenerator;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RuleNode extends GenericNode {
  private ActionErrorNode errorNode;
  private ConditionNode condition;

  public RuleNode(ASTRule rule, DocumentSection section, TransformationParameter parameter) {
    super(section, parameter);
    if (rule == null) return;

    ASTActionError actionError = (ASTActionError) rule.getAction();
    if (actionError != null) {
      this.errorNode = this.generateErrorNode(rule, actionError, parameter);
    }

    if (rule.getCondition() != null) {
      DocumentSection newSection = new RangeGenerator(section).generate(rule.getCondition());
      this.condition =
          NodeGenerator.createConditionNode(rule.getCondition(), newSection, rule, parameter);
    }
  }

  private ActionErrorNode generateErrorNode(
      ASTRule rule, ASTActionError actionError, TransformationParameter parameter) {

    // Look for the "THEN"-Keyword
    List<String> thenKeyword =
        Aliases.getAliasByToken(parameter.getCulture(), Constants.THEN_TOKEN);
    String actionErrorString;

    actionErrorString = actionError.getOriginalSource();
    if (thenKeyword.size() > 0) {
      actionErrorString = thenKeyword.get(0) + " " + actionErrorString;
    }

    DocumentSection section =
        new RangeGenerator(this.getLines(), this.getRange()).generate(actionErrorString);

    // In a constrained rule, the whole rule is the action error
    if (section == null
        && rule.getAllConditions().stream().anyMatch(ASTCondition::isConstrainedCondition)) {
      section =
          new DocumentSection(
              this.getRange(),
              Arrays.asList(actionError.getErrorMessage().split("\n")),
              actionError);
    } else if (section != null) {
      section.setItem(actionError);
    }
    return new ActionErrorNode(section, actionError, parameter);
  }

  public ActionErrorNode getErrorNode() {
    return errorNode;
  }

  public void setErrorNode(ActionErrorNode errorMessage) {
    this.errorNode = errorMessage;
  }

  public ConditionNode getCondition() {
    return condition;
  }

  public void setCondition(ConditionNode condition) {
    this.condition = condition;
  }

  public List<String> getPotentialKeywords() {
    return Collections.singletonList(Constants.IF_TOKEN);
  }
}
