package com.example.parkingspotskopje.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.parkingspotskopje.R
import com.example.parkingspotskopje.viewmodels.ParkingViewModel
import com.google.firebase.auth.FirebaseAuth

class AddNewReviewDialogFragment : DialogFragment() {
    private val parkingViewModel: ParkingViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        val view = inflater.inflate(R.layout.fragment_add_new_review, container, false)
        var starsList=ArrayList<ImageView>()
        var starsValue=0
        starsList.add(view.findViewById<ImageView>(R.id.imageStar1))
        starsList.add(view.findViewById<ImageView>(R.id.imageStar2))
        starsList.add(view.findViewById<ImageView>(R.id.imageStar3))
        starsList.add(view.findViewById<ImageView>(R.id.imageStar4))
        starsList.add(view.findViewById<ImageView>(R.id.imageStar5))
        for(star in starsList){
            star.setOnClickListener{
                var index=starsList.indexOf(star)
                for(s in starsList){
                    if(starsList.indexOf(s)<=index){
                        s.setImageResource(R.drawable.star_full)
                    }
                    else{
                        s.setImageResource(R.drawable.star_empty)
                    }
                    starsValue=index+1;
                }
            }
        }

        var comment=""
        view.findViewById<TextView>(R.id.editTextComment).addTextChangedListener(object :
            TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                comment=s.toString()
            }
        })
        fun showErrorDialog(context: Context) {
            val alertDialog = AlertDialog.Builder(context)
                .setTitle("Be careful!")
                .setMessage("You must write some text and select the number of stars you wish to rate this parking with!")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            alertDialog.show()
        }
        val addReviewButton = view.findViewById<Button>(R.id.buttonAddReview)
        addReviewButton.setOnClickListener {
            if(starsValue==0 || comment==""){
                showErrorDialog(requireContext())
            }else{
                parkingViewModel.parking.value?.let { it1 -> parkingViewModel.addNewReview(it1.id, auth.currentUser?.email!!,auth.currentUser?.displayName!!,starsValue,comment) }
                dismiss()
            }
        }
        return view
    }
}