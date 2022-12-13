/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.scaleph.engine.flink.service;

import cn.sliew.scaleph.engine.flink.service.dto.WsFlinkClusterConfigDTO;
import cn.sliew.scaleph.engine.flink.service.dto.KubernetesOptionsDTO;
import cn.sliew.scaleph.engine.flink.service.param.WsFlinkClusterConfigParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

public interface WsFlinkClusterConfigService {

    int insert(WsFlinkClusterConfigDTO param);

    int updateKubernetesOptions(Long id, KubernetesOptionsDTO options);

    int updateFlinkConfig(Long id, Map<String, String> options);

    int update(WsFlinkClusterConfigDTO dto);

    int deleteById(Long id);

    int deleteBatch(List<Long> ids);

    Page<WsFlinkClusterConfigDTO> listByPage(WsFlinkClusterConfigParam param);

    WsFlinkClusterConfigDTO selectOne(Long id);
}