package com.outing.club.dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.outing.club.entity.Category;
import com.outing.club.entity.Outing;
import com.outing.club.repository.OutingRepository;

import java.util.List;
import java.util.Optional;

@Component
public class OutingDAO {
    @Autowired
    private OutingRepository repository;

    public List<Outing> getOutingsByCategory(Category category) {
        return repository.findByCategory(category);
    }

    public Optional<Outing> getOutingById(Long id) {
        return repository.findById(id);
    }

    public Outing createOrUpdateOuting(Outing outing) {
        return repository.save(outing);
    }

    public void deleteOuting(Long id) {
        repository.deleteById(id);
    }
}
