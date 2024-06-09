package hr.algebra.echoessence.ui.me

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import hr.algebra.echoessence.databinding.FragmentMeBinding

class MeFragment : Fragment() {

    private var _binding: FragmentMeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val meViewModel = ViewModelProvider(requireActivity()).get(MeViewModel::class.java)

        _binding = FragmentMeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        meViewModel.email.observe(viewLifecycleOwner, Observer { email ->
            Log.d("MeFragment", "Email: $email")
        })

        meViewModel.name.observe(viewLifecycleOwner, Observer { name ->
            Log.d("MeFragment", "Name: $name")
        })

        meViewModel.photoUrl.observe(viewLifecycleOwner, Observer { photoUrl ->
            Log.d("MeFragment", "PhotoUrl: $photoUrl")
            // Use Glide to load the image
            Glide.with(this).load(photoUrl).into(binding.profileImg)
        })

        val textView = root.findViewById<TextView>(hr.algebra.echoessence.R.id.textView)
        textView.text = "Email: ${meViewModel.email.value}\nName: ${meViewModel.name.value}"

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
