package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import com.devsuperior.dsmeta.dto.ReportMinDTO;
import com.devsuperior.dsmeta.dto.SummaryMinDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public Page<ReportMinDTO> searchSalesByPeriod(String minDate, String maxDate, String name, Pageable pageable) {
		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());

		LocalDate max = maxDate.equals("")? today : LocalDate.parse(maxDate);
		LocalDate min = minDate.equals("")? today.minusYears(1L) : LocalDate.parse(minDate);

		Page<ReportMinDTO> result = repository.searchSalesByPeriod(min, max, name, pageable);

		//versao que seleciona tudo obj e recebe a entidade
		//Page<Sale> result = repository.searchSalesByPeriod(min, max, name, pageable);
		//return result.map(x -> new ReportMinDTO(x));

		return result;

	}

	public List<SummaryMinDTO> searchTotalSales(String minDate, String maxDate) {
		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());

		LocalDate max = maxDate.equals("")? today : LocalDate.parse(maxDate);
		LocalDate min = minDate.equals("")? today.minusYears(1L) : LocalDate.parse(minDate);

		List<SummaryMinDTO> dto = repository.searchTotalSales(min, max);

		return dto;
	}
}
