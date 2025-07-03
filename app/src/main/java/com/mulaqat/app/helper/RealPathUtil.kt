package com.mulaqat.app.helper

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore

object RealPathUtil {

    fun getRealPath(context: Context, fileUri: Uri): String? {
        return getRealPathFromURI_API19(context, fileUri)
    }

    @SuppressLint("NewApi")
    fun getRealPathFromURI_API19(context: Context, uri: Uri): String? {
        if (DocumentsContract.isDocumentUri(context, uri)) {

            when {
                isExternalStorageDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val (type, path) = docId.split(":")
                    return if (type.equals("primary", true)) {
                        "${Environment.getExternalStorageDirectory()}/$path"
                    } else {
                        // Handle non-primary volumes if needed
                        null
                    }
                }

                isDownloadsDocument(uri) -> {
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        id.toLongOrNull() ?: return null
                    )
                    return getDataColumn(context, contentUri, null, null)
                }

                isMediaDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val (type, id) = docId.split(":")
                    val contentUri = when (type) {
                        "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        else -> null
                    } ?: return null

                    val selection = "_id=?"
                    val selectionArgs = arrayOf(id)
                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }
            }

        } else if (uri.scheme.equals("content", true)) {
            return if (isGooglePhotosUri(uri)) {
                uri.lastPathSegment
            } else {
                getDataColumn(context, uri, null, null)
            }
        } else if (uri.scheme.equals("file", true)) {
            return uri.path
        }

        return null
    }

    private fun getDataColumn(
        context: Context,
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        val projection = arrayOf("_data")
        context.contentResolver.query(uri, projection, selection, selectionArgs, null).use { cursor ->
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow("_data")
                return cursor.getString(index)
            }
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return uri.authority == "com.android.externalstorage.documents"
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return uri.authority == "com.android.providers.downloads.documents"
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return uri.authority == "com.android.providers.media.documents"
    }

    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return uri.authority == "com.google.android.apps.photos.content"
    }
}