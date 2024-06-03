package com.madeean.comicslibrary.model.db

import kotlinx.coroutines.flow.Flow

class CollectionDbRepoImpl(private val characterDao: CharacterDao, private val noteDao: NoteDao) :
  CollectionDbRepo {
  override suspend fun getCharactersFromRepo(): Flow<List<DbCharacter>> {
    return characterDao.getCharacters()
  }

  override suspend fun getCharacterFromRepo(characterId: Int): Flow<DbCharacter> {
    return characterDao.getCharacter(characterId = characterId)
  }

  override suspend fun addCharacterToRepo(character: DbCharacter) {
    return characterDao.addCharacters(character)
  }

  override suspend fun updateCharacterInRepo(character: DbCharacter) {
    return characterDao.updateCharacter(character)
  }

  override suspend fun deleteCharacterFromRepo(character: DbCharacter) {
    return characterDao.deleteCharacter(character)
  }

  override suspend fun getAllNotes(): Flow<List<DbNote>> {
    return noteDao.getAllNotes()
  }

  override suspend fun getNotesFromRepo(characterId: Int): Flow<List<DbNote>> {
    return noteDao.getNotes(characterId)
  }

  override suspend fun addNoteToRepo(note: DbNote) {
    return noteDao.addNote(note)

  }

  override suspend fun updateNoteToRepo(note: DbNote) {
    return noteDao.updateNote(note)

  }

  override suspend fun deleteNoteToRepo(note: DbNote) {
    return noteDao.deleteNotes(note)
  }

  override suspend fun deleteAllNotes(character: DbCharacter) {
    return noteDao.deleteAllNotes(character.id)
  }
}