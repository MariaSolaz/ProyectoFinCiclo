package es.florida.service.impl;

import es.florida.domain.Mecanico;
import es.florida.repository.MecanicoRepository;
import es.florida.service.MecanicoService;
import es.florida.service.dto.MecanicoDTO;
import es.florida.service.mapper.MecanicoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Mecanico}.
 */
@Service
@Transactional
public class MecanicoServiceImpl implements MecanicoService {

    private final Logger log = LoggerFactory.getLogger(MecanicoServiceImpl.class);

    private final MecanicoRepository mecanicoRepository;

    private final MecanicoMapper mecanicoMapper;

    public MecanicoServiceImpl(MecanicoRepository mecanicoRepository, MecanicoMapper mecanicoMapper) {
        this.mecanicoRepository = mecanicoRepository;
        this.mecanicoMapper = mecanicoMapper;
    }

    @Override
    public MecanicoDTO save(MecanicoDTO mecanicoDTO) {
        log.debug("Request to save Mecanico : {}", mecanicoDTO);
        Mecanico mecanico = mecanicoMapper.toEntity(mecanicoDTO);
        mecanico = mecanicoRepository.save(mecanico);
        return mecanicoMapper.toDto(mecanico);
    }

    @Override
    public Optional<MecanicoDTO> partialUpdate(MecanicoDTO mecanicoDTO) {
        log.debug("Request to partially update Mecanico : {}", mecanicoDTO);

        return mecanicoRepository
            .findById(mecanicoDTO.getId())
            .map(
                existingMecanico -> {
                    mecanicoMapper.partialUpdate(existingMecanico, mecanicoDTO);
                    return existingMecanico;
                }
            )
            .map(mecanicoRepository::save)
            .map(mecanicoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MecanicoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Mecanicos");
        return mecanicoRepository.findAll(pageable).map(mecanicoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MecanicoDTO> findOne(Long id) {
        log.debug("Request to get Mecanico : {}", id);
        return mecanicoRepository.findById(id).map(mecanicoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Mecanico : {}", id);
        mecanicoRepository.deleteById(id);
    }
}
