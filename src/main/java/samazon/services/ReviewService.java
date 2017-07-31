package samazon.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import samazon.models.Product;
import samazon.models.Review;
import samazon.repositories.ReviewRepository;

@Service
public class ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	public ReviewService(ReviewRepository reviewRepository) {
		this.reviewRepository = reviewRepository;
	}
	
	public void saveReview(Review review) {
		reviewRepository.save(review);
	}
	
	public List<Review> findByProduct(Product prod) {
		return reviewRepository.findByProduct(prod);
	}
}
