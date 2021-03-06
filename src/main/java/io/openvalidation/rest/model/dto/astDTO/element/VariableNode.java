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

import io.openvalidation.common.ast.ASTVariable;
import io.openvalidation.rest.model.dto.astDTO.GenericNode;
import io.openvalidation.rest.model.dto.astDTO.TransformationParameter;
import io.openvalidation.rest.model.dto.astDTO.operation.operand.OperandNode;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import io.openvalidation.rest.model.dto.astDTO.transformation.NodeGenerator;
import io.openvalidation.rest.model.dto.astDTO.transformation.RangeGenerator;
import java.util.ArrayList;
import java.util.List;

public class VariableNode extends GenericNode {
  private OperandNode value;
  private VariableNameNode nameNode;

  public VariableNode(
      ASTVariable variable, DocumentSection section, TransformationParameter parameter) {
    super(section, parameter);
    if (variable == null) return;

    DocumentSection operandSection = new RangeGenerator(section).generate(variable.getValue());

    String variableNameString = variable.getOriginalSource();
    if (variable.getValue() != null) {
      this.value = NodeGenerator.createOperand(variable.getValue(), operandSection, parameter);
      variableNameString =
          variableNameString
              .substring(variable.getValue().getOriginalSource().length())
              .replace("\n", "");
    }
    variableNameString = variableNameString.trim();

    DocumentSection variableSection = new RangeGenerator(section).generate(variableNameString);
    this.nameNode = new VariableNameNode(variableSection, variable.getName(), parameter);
  }

  public VariableNameNode getNameNode() {
    return nameNode;
  }

  public void setNameNode(VariableNameNode nameNode) {
    this.nameNode = nameNode;
  }

  public OperandNode getValue() {
    return value;
  }

  public void setValue(OperandNode value) {
    this.value = value;
  }

  @Override
  public List<String> getPotentialKeywords() {
    return new ArrayList<>();
  }
}
