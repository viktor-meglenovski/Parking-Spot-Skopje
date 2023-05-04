package com.example.parkingspotskopje.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.databinding.FragmentParkingReviewsBinding
import com.example.parkingspotskopje.domain.adapters.OnItemClickListener
import com.example.parkingspotskopje.domain.adapters.ReviewsListAdapter
import com.example.parkingspotskopje.domain.model.Review
import com.example.parkingspotskopje.ui.dialogs.AddNewReviewDialogFragment
import com.example.parkingspotskopje.viewmodels.ParkingViewModel

class ParkingReviewsFragment: Fragment(R.layout.fragment_parking_reviews) {
    private var _binding:FragmentParkingReviewsBinding?=null
    private val binding get()=_binding!!

    private lateinit var reviewsListAdapter: ReviewsListAdapter

    private val parkingViewModel: ParkingViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding=FragmentParkingReviewsBinding.bind(view)

        binding.addReviewBtn.setOnClickListener{
            val dialog = AddNewReviewDialogFragment()
            dialog.show(parentFragmentManager, "AddReviewDialog")
        }

        binding.recyclerViewListReviews.layoutManager=LinearLayoutManager(context)

        val clicker = object: OnItemClickListener {
            override fun onItemClick(obj: Any) {
                var review=obj as Review
            }
        }
        reviewsListAdapter=ReviewsListAdapter(ArrayList<Review>(),clicker)
        binding.recyclerViewListReviews.adapter=reviewsListAdapter

        parkingViewModel.reviews.observe(viewLifecycleOwner){
            reviewsListAdapter.updateList(it.sortedByDescending { x->x.timestamp })
        }

        //TODO if no reviews, add message

    }

}