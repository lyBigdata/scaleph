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

package cn.sliew.scaleph.dao.entity.master.ws;

import cn.sliew.scaleph.common.dict.flink.FlinkDeploymentMode;
import cn.sliew.scaleph.common.dict.flink.FlinkResourceProvider;
import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import cn.sliew.scaleph.dao.entity.BaseDO;
import cn.sliew.scaleph.dao.entity.master.resource.ResourceClusterCredential;
import cn.sliew.scaleph.dao.entity.master.resource.ResourceFlinkRelease;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * flink 集群配置
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ws_flink_cluster_config")
@ApiModel(value = "FlinkClusterConfig对象", description = "flink 集群配置")
public class WsFlinkClusterConfig extends BaseDO {

    private static final long serialVersionUID = 1L;

    @TableField("project_id")
    private Long projectId;

    @ApiModelProperty("名称")
    @TableField("`name`")
    private String name;

    @ApiModelProperty("集群版本")
    @TableField("flink_version")
    private FlinkVersion flinkVersion;

    @ApiModelProperty("Resource。0: Standalone, 1: Native Kubernetes, 2: YARN")
    @TableField("resource_provider")
    private FlinkResourceProvider resourceProvider;

    @ApiModelProperty("flink 部署模式。0: Application, 1: Per-Job, 2: Session")
    @TableField("deploy_mode")
    private FlinkDeploymentMode deployMode;

    @ApiModelProperty("flink release")
    @TableField(exist = false)
    private ResourceFlinkRelease flinkRelease;

    @TableField("flink_release_id")
    private Long FlinkReleaseId;

    @ApiModelProperty("集群凭证 id。如 hadoop 的 core-site.xml，kubernetes 的 kubeconfig")
    @TableField(exist = false)
    private ResourceClusterCredential clusterCredential;

    @TableField("cluster_credential_id")
    private Long clusterCredentialId;

    @ApiModelProperty("kubernetes 配置")
    @TableField("kubernetes_options")
    private String kubernetesOptions;

    @ApiModelProperty("flink 集群配置项")
    @TableField("config_options")
    private String configOptions;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;
}