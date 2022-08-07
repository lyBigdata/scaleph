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

package cn.sliew.scaleph.engine.seatunnel.service.impl;

import cn.sliew.scaleph.dao.entity.master.resource.ReleaseSeaTunnel;
import cn.sliew.scaleph.dao.mapper.master.resource.ReleaseSeaTunnelMapper;
import cn.sliew.scaleph.engine.seatunnel.service.ReleaseSeaTunnelService;
import cn.sliew.scaleph.engine.seatunnel.service.convert.ReleaseSeaTunnelConvert;
import cn.sliew.scaleph.engine.seatunnel.service.dto.ReleaseSeaTunnelDTO;
import cn.sliew.scaleph.engine.seatunnel.service.param.ReleaseSeaTunnelListParam;
import cn.sliew.scaleph.engine.seatunnel.service.param.ReleaseSeaTunnelUploadParam;
import cn.sliew.scaleph.storage.service.FileSystemService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class ReleaseSeaTunnelServiceImpl implements ReleaseSeaTunnelService {

    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private ReleaseSeaTunnelMapper releaseSeaTunnelMapper;

    @Override
    public Page<ReleaseSeaTunnelDTO> list(ReleaseSeaTunnelListParam param) throws IOException {
        final Page<ReleaseSeaTunnel> page = releaseSeaTunnelMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(ReleaseSeaTunnel.class)
                        .eq(StringUtils.hasText(param.getVersion()), ReleaseSeaTunnel::getVersion, param.getVersion())
                        .like(StringUtils.hasText(param.getFileName()), ReleaseSeaTunnel::getFileName, param.getFileName()));
        Page<ReleaseSeaTunnelDTO> result =
                new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<ReleaseSeaTunnelDTO> dtoList = ReleaseSeaTunnelConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public ReleaseSeaTunnelDTO selectOne(Long id) {
        final ReleaseSeaTunnel record = releaseSeaTunnelMapper.selectById(id);
        checkState(record != null, () -> "release seatunnel not exists for id: " + id);
        return ReleaseSeaTunnelConvert.INSTANCE.toDto(record);
    }

    @Override
    public void upload(ReleaseSeaTunnelUploadParam param, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String filePath = getReleaseSeaTunnelPath(param.getVersion(), fileName);
        try (final InputStream inputStream = file.getInputStream()) {
            fileSystemService.upload(inputStream, filePath);
        }
        ReleaseSeaTunnel record = new ReleaseSeaTunnel();
        BeanUtils.copyProperties(param, record);
        record.setFileName(fileName);
        record.setPath(filePath);
        releaseSeaTunnelMapper.insert(record);
    }

    @Override
    public String download(Long id, OutputStream outputStream) throws IOException {
        final ReleaseSeaTunnelDTO dto = selectOne(id);
        try (final InputStream inputStream = fileSystemService.get(dto.getPath())) {
            FileCopyUtils.copy(inputStream, outputStream);
        }
        return dto.getFileName();
    }

    @Override
    public int deleteBatch(List<Long> ids) throws IOException {
        for (Serializable id : ids) {
            delete((Long) id);
        }
        return ids.size();
    }

    @Override
    public void delete(Long id) throws IOException {
        final ReleaseSeaTunnelDTO dto = selectOne(id);
        fileSystemService.delete(dto.getPath());
        releaseSeaTunnelMapper.deleteById(id);
    }

    private String getReleaseSeaTunnelPath(String version, String fileName) {
        return String.format("%s/%s/%s", getReleaseSeaTunnelRootPath(), version, fileName);
    }

    private String getReleaseSeaTunnelRootPath() {
        return "release/seatunnel";
    }
}