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

        val textView = root.findViewById<TextView>(hr.algebra.echoessence.R.id.textView)
        textView.text = "${meViewModel.email.value}\n${meViewModel.name.value}"

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
