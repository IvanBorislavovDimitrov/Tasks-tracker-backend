package com.tracker.taskstracker.service.impl;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tracker.taskstracker.domain.IdEntity;
import com.tracker.taskstracker.exception.TRException;
import com.tracker.taskstracker.model.response.IdModel;
import com.tracker.taskstracker.service.api.GenericService;

public abstract class GenericServiceImpl<E extends IdEntity, IM, OM extends IdModel, ID> implements GenericService<IM, OM, ID> {

    protected final ModelMapper modelMapper;
    private final JpaRepository<E, ID> repository;

    protected GenericServiceImpl(JpaRepository<E, ID> repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<OM> findAll() {
        return repository.findAll()
                         .stream()
                         .map(entity -> modelMapper.map(entity, getOutputModelClass()))
                         .collect(Collectors.toList());
    }

    @Override
    public OM findById(ID id) {
        E entity = findEntityById(id);
        return modelMapper.map(entity, getOutputModelClass());
    }

    @Override
    public OM save(IM model) {
        E entity = modelMapper.map(model, getEntityClass());
        E savedEntity = repository.save(entity);
        return modelMapper.map(savedEntity, getOutputModelClass());
    }

    @Override
    public OM deleteById(ID id) {
        E entityById = findEntityById(id);
        repository.deleteById(id);
        return modelMapper.map(entityById, getOutputModelClass());
    }

    @Override
    public OM update(IM model) {
        return save(model);
    }

    @Override
    public long count() {
        return repository.count();
    }

    private E findEntityById(ID id) {
        Optional<E> entity = repository.findById(id);
        if (entity.isEmpty()) {
            throw new TRException(MessageFormat.format("Searched entity not found: \"{0}\"", id));
        }
        return entity.get();
    }

    protected abstract Class<OM> getOutputModelClass();

    protected abstract Class<E> getEntityClass();
}
