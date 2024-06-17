package com.devsuperior.demo.services;

import com.devsuperior.demo.dto.CityDTO;
import com.devsuperior.demo.entities.City;
import com.devsuperior.demo.repositories.CityRepository;
import com.devsuperior.demo.services.exceptions.DatabaseException;
import com.devsuperior.demo.services.exceptions.ResourceNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CityService {

  @Autowired private CityRepository repository;

  @Transactional(readOnly = true)
  public List<CityDTO> findAll() {
    List<City> list = repository.findAll(Sort.by("name"));
    return list.stream().map(CityDTO::new).toList();
  }

  @Transactional
  public CityDTO create(CityDTO dto) {
    City entity = new City();
    entity.setName(dto.getName());
    entity = repository.save(entity);
    return new CityDTO(entity);
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  public void delete(Long id) {
    if (!repository.existsById(id)) {
      throw new ResourceNotFoundException("Id not found " + id);
    }
    try {
      repository.deleteById(id);
    } catch (DataIntegrityViolationException e) {
      throw new DatabaseException("Referenced integrity failure");
    }
  }
}
