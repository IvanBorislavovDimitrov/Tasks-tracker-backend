package com.tracker.taskstracker.service.api;

import java.util.List;

import com.tracker.taskstracker.model.response.IdModel;

public interface GenericService<IM, OM extends IdModel, ID> {

    List<OM> findAll();

    OM findById(ID id);

    OM save(IM model);

    OM deleteById(ID id);

    OM update(IM model);

    long count();
}
