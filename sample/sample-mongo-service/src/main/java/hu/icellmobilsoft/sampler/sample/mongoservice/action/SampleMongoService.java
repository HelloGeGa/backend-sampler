/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 i-Cell Mobilsoft Zrt.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package hu.icellmobilsoft.sampler.sample.mongoservice.action;

import javax.enterprise.context.Dependent;

import hu.icellmobilsoft.coffee.module.mongodb.service.MongoService;

/**
 * Sample MongoService implementation by Mongo module
 * 
 * @see <a href="https://i-cell-mobilsoft-open-source.github.io/coffee/#common_module_coffee-module-mongodb">Mongo module</a>
 * @author imre.scheffer
 * @since 0.1.0
 */
@Dependent
public class SampleMongoService extends MongoService<String> {

}
