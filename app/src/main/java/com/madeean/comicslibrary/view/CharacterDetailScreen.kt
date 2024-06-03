package com.madeean.comicslibrary.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.madeean.comicslibrary.CharacterImage
import com.madeean.comicslibrary.Destination
import com.madeean.comicslibrary.comicsToString
import com.madeean.comicslibrary.model.db.CollectionDbRepo
import com.madeean.comicslibrary.viewmodel.CollectionDbViewModel
import com.madeean.comicslibrary.viewmodel.LibraryApiViewModel

@Composable
fun CharacterDetailScreen(
  lvm: LibraryApiViewModel,
  cvm: CollectionDbViewModel,
  paddingValues: PaddingValues,
  navController: NavHostController
) {
  val character = lvm.characterDetail.value
  val collection by cvm.collection.collectAsState()
  val inCollection = collection.map {
    it.apiId
  }.contains(character?.id)


  if (character == null) {
    navController.navigate(Destination.Library.route) {
      popUpTo(Destination.Library.route)
      launchSingleTop = true
    }
  }

  LaunchedEffect(key1 = Unit) {
    cvm.setCurrentCharacterId(character?.id)
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(4.dp)
      .padding(bottom = paddingValues.calculateBottomPadding())
      .verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {

    val imageUrl = character?.thumbnail?.path + "." + character?.thumbnail?.extension
    val title = character?.name ?: "no name"
    val comics = character?.comics?.items?.mapNotNull { it.name }?.comicsToString() ?: "no comics"
    val description = character?.description ?: "no description"

    CharacterImage(
      url = imageUrl, modifier = Modifier
        .width(200.dp)
        .padding(4.dp)
    )

    Text(
      text = title,
      fontWeight = FontWeight.Bold,
      fontSize = 30.sp,
      modifier = Modifier.padding(4.dp)
    )
    Text(
      text = comics,
      fontStyle = FontStyle.Italic,
      fontSize = 12.sp,
      modifier = Modifier.padding(4.dp)
    )
    Text(text = description, fontSize = 16.sp, modifier = Modifier.padding(bottom = 20.dp))

    Button(onClick = {
      if (!inCollection && character != null) {
        cvm.addCharacter(character)
      }
    }, modifier = Modifier.padding(bottom = 20.dp)) {

      if (!inCollection) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
          Text(text = "Add to collection", color = Color.White)
        }
      } else {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
          Text(text = "Added", color = Color.White)
        }
      }

    }
  }
}