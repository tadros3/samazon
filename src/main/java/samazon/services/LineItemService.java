package samazon.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import samazon.models.LineItem;
import samazon.models.Order;
import samazon.repositories.LineItemRepository;


@Service
public class LineItemService {
	@Autowired
	private LineItemRepository line;
	
	public void saveLineItem(LineItem litem)
	{
		line.save(litem);
	}
}
