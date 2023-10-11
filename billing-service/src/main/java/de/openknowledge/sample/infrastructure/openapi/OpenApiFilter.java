/*
 * Copyright 2019 - 2023 open knowledge GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.openknowledge.sample.infrastructure.openapi;

import java.util.Map.Entry;

import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.Components;
import org.eclipse.microprofile.openapi.models.OpenAPI;
import org.eclipse.microprofile.openapi.models.PathItem;
import org.eclipse.microprofile.openapi.models.media.Content;
import org.eclipse.microprofile.openapi.models.parameters.RequestBody;
import org.eclipse.microprofile.openapi.models.responses.APIResponse;

public class OpenApiFilter implements OASFilter {

	public void filterOpenAPI(OpenAPI openApi) {
		for (Entry<String, PathItem> item: openApi.getPaths().getPathItems().entrySet()) {
			if (item.getKey().startsWith("/metrics") || item.getKey().startsWith("/health") || item.getKey().contains("webjars")) {
				openApi.getPaths().removePathItem(item.getKey());
			}
		}
		Components components = openApi.getComponents();
		components.removeSchema("void");
        components.removeSchema("java_lang_Object");
	}
	
	@Override
    public RequestBody filterRequestBody(RequestBody requestBody) {
        requestBody.setContent(filterContent(requestBody.getContent()));
        return requestBody;
    }

    @Override
    public APIResponse filterAPIResponse(APIResponse apiResponse) {
        apiResponse.setContent(filterContent(apiResponse.getContent()));
        return apiResponse;
    }

    public Content filterContent(Content content) {
        content.removeMediaType("application/json");
        content.removeMediaType("application/vnd.de.openknowledge.sample.address.v1+json");
        return content;
    }
}