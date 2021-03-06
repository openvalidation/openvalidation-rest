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

package io.openvalidation.rest.model.dto.astDTO;

import java.util.ArrayList;
import java.util.List;

public class MainNode {
  private List<Variable> declarations;
  private List<GenericNode> scopes;
  private Range range;

  public MainNode() {
    this.declarations = new ArrayList<>();
    this.scopes = new ArrayList<>();
  }

  public List<Variable> getDeclarations() {
    return declarations;
  }

  public void addDeclarations(List<Variable> declarations) {
    this.declarations.addAll(declarations);
  }

  public List<GenericNode> getScopes() {
    return scopes;
  }

  public void addScope(GenericNode scope) {
    this.scopes.add(scope);
  }

  public void addScopes(List<GenericNode> scopes) {
    this.scopes.addAll(scopes);
  }

  public Range getRange() {
    return range;
  }

  public void setRange(Range range) {
    this.range = range;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof MainNode) {
      return (((MainNode) obj).range == null && this.range == null
              || ((MainNode) obj).range.equals(this.range))
          && ((MainNode) obj).declarations.equals(this.declarations)
          && ((MainNode) obj).scopes.equals(this.scopes);
    }
    return false;
  }
}
