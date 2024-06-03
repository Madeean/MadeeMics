package com.madeean.comicslibrary

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.madeean.comicslibrary.ui.theme.ComicsLibraryTheme
import com.madeean.comicslibrary.view.CharacterDetailScreen
import com.madeean.comicslibrary.view.CharactersBottomNav
import com.madeean.comicslibrary.view.CollectionScreen
import com.madeean.comicslibrary.view.LibraryScreen
import com.madeean.comicslibrary.viewmodel.CollectionDbViewModel
import com.madeean.comicslibrary.viewmodel.LibraryApiViewModel
import dagger.hilt.android.AndroidEntryPoint

sealed class Destination(val route: String) {
  data object Library : Destination("library")
  data object Collection : Destination("collection")
  data object CharacterDetail : Destination("character/{characterId}") {
    fun createRoute(characterId: Int?) = "character/$characterId"
  }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val lvm by viewModels<LibraryApiViewModel>()
  private val cvm by viewModels<CollectionDbViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      ComicsLibraryTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          val navController = rememberNavController()
          CharactersScaffold(navController = navController, lvm = lvm, cvm = cvm)
        }
      }
    }
  }
}

@Composable
fun CharactersScaffold(
  navController: NavHostController,
  modifier: Modifier = Modifier,
  lvm: LibraryApiViewModel,
  cvm: CollectionDbViewModel
) {
  val scaffoldState = rememberScaffoldState()
  val ctx = LocalContext.current

  androidx.compose.material.Scaffold(
    scaffoldState = scaffoldState,
    bottomBar = { CharactersBottomNav(navController = navController) }
  ) { paddingValues ->
    NavHost(navController = navController, startDestination = Destination.Library.route) {
      composable(Destination.Library.route) {
        LibraryScreen(navController, lvm, paddingValues)
      }
      composable(Destination.Collection.route) {
        CollectionScreen(cvm = cvm, navController = navController)
      }
      composable(Destination.CharacterDetail.route) {
        val id = it.arguments?.getString("characterId")?.toIntOrNull()
        if (id == null) {
          Toast.makeText(ctx, "Character id is required", Toast.LENGTH_SHORT).show()
        } else {
          lvm.retrieveSingleCharacter(id)
          CharacterDetailScreen(
            lvm = lvm,
            paddingValues = paddingValues,
            navController = navController,
            cvm = cvm
          )
        }
      }
    }
  }
}