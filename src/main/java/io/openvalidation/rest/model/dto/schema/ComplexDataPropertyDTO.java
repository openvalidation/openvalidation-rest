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

package io.openvalidation.rest.model.dto.schema;

import io.openvalidation.common.data.DataProperty;

public class ComplexDataPropertyDTO {

  private String parent;
  private String child;

  public ComplexDataPropertyDTO(DataProperty property) {
    this.parent = property.getPath();
    this.child = property.getName();
  }

  public String getParent() {
    return parent;
  }

  public void setParent(String parent) {
    this.parent = parent;
  }

  public String getChild() {
    return child;
  }

  public void setChild(String child) {
    this.child = child;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ComplexDataPropertyDTO) {
      return this.parent.equals(((ComplexDataPropertyDTO) obj).parent)
          && this.child.equals(((ComplexDataPropertyDTO) obj).child);
    }
    return false;
  }
}
