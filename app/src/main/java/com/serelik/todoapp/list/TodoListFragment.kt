package com.serelik.todoapp.list

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.serelik.todoapp.MainActivity
import com.serelik.todoapp.R
import com.serelik.todoapp.databinding.FragmentTodoListBinding
import com.serelik.todoapp.di.TodoListFragmentComponent
import com.serelik.todoapp.edit.TodoEditFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

class TodoListFragment : Fragment(R.layout.fragment_todo_list) {

    private val viewModel: TodoListViewModel by viewModels() {
        viewModelFactory
    }

    lateinit var component: TodoListFragmentComponent


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val todoItemAdapter by lazy {
        TodoItemAdapter(
            onTodoClickListener = ::openEditFragment,
            changeIsDoneListener = viewModel::changedStateDone,
            onNewTodoClickListener = ::openAddFragment
        )
    }

    private val viewBinding by viewBinding(FragmentTodoListBinding::bind)

    private val supportFragmentManager by lazy { requireActivity().supportFragmentManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component = (requireActivity() as MainActivity)
            .activityComponent
            .todoListFragmentComponent().create()

        component.inject(this)

        viewModel.changeDoneVisibility()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.flowLoading.collectLatest { status ->
                    when (status) {
                        is LoadingStatus.Error -> {
                            Toast.makeText(
                                requireContext(),
                                "Нет интернета",
                                Toast.LENGTH_SHORT
                            ).show()
                            viewBinding.swipeRefreshLayout.isRefreshing = false
                        }

                        LoadingStatus.Loading -> {
                            viewBinding.swipeRefreshLayout.isRefreshing = true
                        }

                        LoadingStatus.Success -> viewBinding.swipeRefreshLayout.isRefreshing = false


                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.liveData.observe(viewLifecycleOwner) { model ->
            todoItemAdapter.submitList(model.items)
            updateVisibilityTodoDoneItemsStatus(model.isDoneVisible)
            viewBinding.textViewDoneCount.text =
                getString(R.string.is_done_count, model.doneCount)
            viewBinding.textViewDoneCount.isInvisible = !model.isDoneVisible
        }

        val recyclerView = viewBinding.recyclerView

        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = todoItemAdapter

        getVisibilityTodoItemButton()?.setOnMenuItemClickListener {
            viewModel.changeDoneVisibility()
            true
        }
        viewBinding.floatingActionButton.setOnClickListener {
            openAddFragment()
        }

        viewBinding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadListFromServer()
        }
        swipeFunctionality()
    }

    private fun getVisibilityTodoItemButton(): MenuItem? {
        return viewBinding.toolbar.menu.findItem(R.id.action_hide)
    }

    private fun updateVisibilityTodoDoneItemsStatus(isDoneVisible: Boolean) {
        val notificationIcon = if (isDoneVisible) {
            R.drawable.ic_visibility_off
        } else {
            R.drawable.ic_visibility_on
        }
        getVisibilityTodoItemButton()?.setIcon(notificationIcon)
    }

    private fun openEditFragment(id: String) {
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, TodoEditFragment.createFragment(id))
            .addToBackStack("Edit_id_Key")
            .commit()
    }

    private fun swipeFunctionality() {
        val displayMetrics: DisplayMetrics = resources.displayMetrics
        val width = (displayMetrics.widthPixels / displayMetrics.density).toInt().dp

        val swipeDeleteIcon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_swipe_delete, null)
        val swipeCheckedIcon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_swipe_checked, null)

        val deleteColor = ContextCompat.getColor(requireContext(), R.color.red)
        val checkedDoneColor = ContextCompat.getColor(requireContext(), R.color.green)

        val swipeHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            val rect = Rect().apply {
                left = 0
                right = width
            }
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (viewHolder !is TodoItemViewHolder)
                    return

                val pos = viewHolder.adapterPosition
                val item = todoItemAdapter.getItemTodo(pos)


                if (direction == ItemTouchHelper.LEFT) {
                    viewModel.remove(item.id)
                } else
                    viewModel.changedStateDone(item, !item.isDone)

            }

            override fun onChildDraw(
                canvas: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (viewHolder !is TodoItemViewHolder)
                    return

                paint.color = when {
                    dX < 0 -> deleteColor
                    else -> checkedDoneColor
                }

                rect.apply {
                    top = viewHolder.itemView.top
                    bottom = viewHolder.itemView.bottom
                }

                canvas.drawRect(rect, paint)

                val iconMargin = resources.getDimension(R.dimen.DeleteDrawableMargin)
                    .roundToInt()
                val drawableHeight = swipeDeleteIcon?.intrinsicHeight ?: 0
                val verticalPadding = (viewHolder.itemView.height - drawableHeight) / 2
                if (swipeCheckedIcon != null) {
                    swipeCheckedIcon.bounds = Rect(
                        iconMargin,
                        viewHolder.itemView.top + verticalPadding,
                        iconMargin + swipeCheckedIcon.intrinsicWidth,
                        viewHolder.itemView.top + swipeCheckedIcon.intrinsicHeight + verticalPadding
                    )
                }
                if (swipeDeleteIcon != null) {
                    swipeDeleteIcon.bounds = Rect(
                        width - iconMargin * 2 - swipeDeleteIcon.intrinsicWidth,
                        viewHolder.itemView.top + verticalPadding,
                        width - iconMargin * 2,
                        viewHolder.itemView.top + swipeDeleteIcon.intrinsicHeight
                                + verticalPadding
                    )
                }

                if (dX < 0) swipeDeleteIcon?.draw(canvas) else swipeCheckedIcon?.draw(canvas)

                super.onChildDraw(
                    canvas,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

        })

        swipeHelper.attachToRecyclerView(viewBinding.recyclerView)

    }

    private fun openAddFragment() {
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, TodoEditFragment())
            .addToBackStack("Todo add fragment")
            .commit()

    }

    private val Int.dp
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            toFloat(), resources.displayMetrics
        ).roundToInt()


}