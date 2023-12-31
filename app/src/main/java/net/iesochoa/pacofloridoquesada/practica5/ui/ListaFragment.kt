package net.iesochoa.pacofloridoquesada.practica5.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import net.iesochoa.pacofloridoquesada.practica5.R
import net.iesochoa.pacofloridoquesada.practica5.databinding.FragmentListaBinding
import net.iesochoa.pacofloridoquesada.practica5.model.Tarea
import net.iesochoa.pacofloridoquesada.practica5.viewmodel.AppViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ListaFragment : Fragment() {

    private var _binding: FragmentListaBinding? = null

    private val binding get() = _binding!!

    private val viewModel: AppViewModel by activityViewModels()

    /**
     * Método que inicia ambos filtros
     */
    private fun iniciaFiltros() {
        binding.rgFiltroEstados.setOnCheckedChangeListener() {_, checkedId->
           when (checkedId) {
               R.id.rbFiltroAbierta -> viewModel.setEstado(0)
               R.id.rbFiltroEnCurso -> viewModel.setEstado(1)
               R.id.rbFIltroCerrada -> viewModel.setEstado(2)
               else -> viewModel.setEstado(3)
           }
        }
        binding.swSinPagar.setOnCheckedChangeListener(){_, isChecked ->
            viewModel.setSoloSinPagar(isChecked)
        }
    }

    /**
     * Método que actualiza la lista por si se ha añadido alguna tarea nueva
     */
    private fun actualizaLista(lista: List<Tarea>?) {
        var listaString=""
        lista?.forEach(){
            listaString="$listaString ${it.id}-${it.tecnico}-${it.descripcion}-${if(it.pagado) "pagado" else
                "no pagado"}\n"
        }
        binding.tvLista.setText(listaString)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentListaBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_ListaFragment_to_TareaFragment)
        }

         */
        viewModel.tareasLiveData.observe(viewLifecycleOwner, Observer<List<Tarea>>{lista ->
            actualizaLista(lista)
        })
        // Indicamos la accion que realizará el boton FabAñadirTarea
        binding.fabAAdirTarea.setOnClickListener{
            val action = ListaFragmentDirections.actionEditar(null)
            findNavController().navigate(action)
        }
        // Indicamos la acción que realizará el botón PruebaEdición
        binding.btPruebaEdicion.setOnClickListener{
            //cogemos la lista actual de Tareas que tenemos en el ViewModel. No es lo más correcto
            val lista = viewModel.tareasLiveData.value
            //buscamos una tarea aleatoriamente
            val tarea=lista?.get((0..lista.lastIndex).random())
            //se la enviamos a TareaFragment para su edición
            val action=ListaFragmentDirections.actionEditar(tarea)
            findNavController().navigate(action)
        }
        iniciaFiltros()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}