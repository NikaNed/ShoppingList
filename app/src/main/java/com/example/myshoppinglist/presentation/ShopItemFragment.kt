package com.example.myshoppinglist.presentation

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myshoppinglist.databinding.FragmentShopItemBinding
import com.example.myshoppinglist.di.ViewModelFactory
import com.example.myshoppinglist.domain.ShopItem
import javax.inject.Inject
import kotlin.concurrent.thread

class ShopItemFragment : Fragment() {

    private lateinit var viewModel: ShopItemViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private val component by lazy {
        (requireActivity().application as ShopListApp).component
    }

    private var _binding: FragmentShopItemBinding? = null
    private val binding: FragmentShopItemBinding
        get() = _binding ?: throw RuntimeException("FragmentShopItemBinding == null")

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINDED_ID

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
        if (context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams() //проверяем параметры
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this,
            viewModelFactory)[ShopItemViewModel::class.java] //инициализируем vM
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        addTextChangedListener() //добавляем слушатели ввода текста
        launchRightMode() //запускаем правильный режим экрана
        observeViewModel() //подписываемся на все объекты viewModel
    }

    private fun parseParams() {
        val args =
            requireArguments() //получаем переданные аргументы, если там будет null, то упадем
        if (!args.containsKey(SCREEN_MODE)) { //если не содержится SCREEN_MODE, то падаем
            throw RuntimeException("Param screen mode absent")
        }
        val mode = args.getString(SCREEN_MODE)//если содержится SCREEN_MODE
        if (mode != MODE_ADD && mode != MODE_EDIT) { //но непонятный режим экрана, то падаем
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) { //если находимся в режиме редактирования
            if (!args.containsKey(SHOP_ITEM_ID)) { //но не нашли SHOP_ITEM_ID, то падаем
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINDED_ID)
        }
    }

    private fun observeViewModel() {
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
        }
    }

    private fun addTextChangedListener() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int,
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int,
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        binding.saveButton.setOnClickListener {
            /*    viewModel.editShopItem(
                    binding.etName.text?.toString(),
                    binding.etCount.text?.toString()
                )*/
            thread {
                val updateValues = ContentValues().apply {
                    put(INSERT_ID, 0)
                    put(INSERT_NAME, binding.etName.text?.toString())
                    put(INSERT_COUNT, binding.etCount.text?.toString()?.toInt())
                    put(INSERT_ENABLED, true)
                }

                val selectionArgs: Array<String> = arrayOf(shopItemId.toString())

                context?.contentResolver?.update(
                    Uri.parse("content://com.example.myshoppinglist/shop_item"),
                    updateValues,
                    null,
                    selectionArgs)
            }
        }
    }

    private fun launchAddMode() {
        binding.saveButton.setOnClickListener {
            /* viewModel.addShopItem(
                 binding.etName.text?.toString(),
                 binding.etCount.text?.toString())*/
            thread {
                context?.contentResolver?.insert(
                    Uri.parse("content://com.example.myshoppinglist/shop_item"),
                    ContentValues().apply {
                        put(INSERT_ID, 0)
                        put(INSERT_NAME, binding.etName.text?.toString())
                        put(INSERT_COUNT, binding.etCount.text?.toString()?.toInt())
                        put(INSERT_ENABLED, true)
                    }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnEditingFinishedListener {
        fun onEditingFinished()
    }

    companion object {
        private const val SCREEN_MODE = "extra_mode"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_UNKNOWN = ""
        const val INSERT_ID = "id"
        const val INSERT_NAME = "name"
        const val INSERT_COUNT = "count"
        const val INSERT_ENABLED = "enabled"

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply { // вернем экземпляр фрагмента с аргументами
                arguments = Bundle().apply { //вызывается arguments и
                    putString(SCREEN_MODE, MODE_ADD) //у него устанавливается новое значение
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }
}
