package com.lemon.smartcampus.ui.toolBtnPage.characterPage

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lemon.smartcampus.ui.widges.CharacterCard
import com.lemon.smartcampus.ui.widges.SNACK_ERROR
import com.lemon.smartcampus.ui.widges.SNACK_SUCCESS
import com.lemon.smartcampus.viewModel.character.CharacterViewAction
import com.lemon.smartcampus.viewModel.character.CharacterViewEvent
import com.lemon.smartcampus.viewModel.character.CharacterViewModel
import com.zj.mvi.core.observeEvent

@Composable
fun CharacterListPage(
    showToast: (String, String) -> Unit,
    navToCharacterDetail: (String, String, String) -> Unit = { _, _, _ -> },
    viewModel: CharacterViewModel = viewModel()
) {
    val state by viewModel.viewStates.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val lazyState = rememberLazyListState()

    LaunchedEffect(key1 = Unit) {
        viewModel.dispatch(CharacterViewAction.GetPage(false))
        viewModel.viewEvents.observeEvent(lifecycleOwner) {
            when (it) {
                is CharacterViewEvent.ShowToast -> showToast(
                    it.msg,
                    if (it.success) SNACK_SUCCESS else SNACK_ERROR
                )
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp, horizontal = 20.dp),
        state = lazyState
    ) {
        items(state.itemList) {
            CharacterCard(entity = it) { navToCharacterDetail(it.name, it.introduction, it.imgUrl) }
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}