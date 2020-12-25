package com.tracker.taskstracker.service.api;

import com.tracker.taskstracker.model.response.IdModel;

import java.util.List;

public interface GenericService<IM, OM extends IdModel, ID> {

    List<OM> findAll();

    OM findById(ID id);

    OM save(IM model);

    OM deleteById(ID id);

    OM update(ID id, IM model);

    long count();
}
