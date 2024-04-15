package com.example.nasaphotos

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nasaphotos.data.GalleryItem

import com.example.nasaphotos.api.ThumbnailDownloader
import com.example.nasaphotos.data.PhotoViewModel


class NasaPhotosFragment: Fragment() {

    private val TAG = "NasaPhotosFragment"
    private lateinit var photoViewModel:PhotoViewModel
    private lateinit var recyclerView:RecyclerView
    private lateinit var thumbnailDownloader: ThumbnailDownloader<PhotoHolder>
    private lateinit var layoutInflater: LayoutInflater



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        photoViewModel = ViewModelProvider(this)[PhotoViewModel::class.java]
        layoutInflater = LayoutInflater.from(context)
        val responseHandler = Handler(Looper.getMainLooper())
        val context = requireContext()
        thumbnailDownloader = ThumbnailDownloader(context,responseHandler){photoHolder,bitmap ->
            val drawable = BitmapDrawable(resources,bitmap)
            photoHolder.bindDrawable(drawable)
        }
        lifecycle.addObserver(thumbnailDownloader)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.nasa_photos_fragment,container,false)
        recyclerView = view.findViewById(R.id.photo_recycler_view) as RecyclerView
        recyclerView.layoutManager = GridLayoutManager(context,3)

//        updateUI()


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photoViewModel.galleryItemLiveData.observe(
            viewLifecycleOwner,
            Observer { galleryItems ->
                recyclerView.adapter = PhotoAdapter(galleryItems)

            }

        )



    }



    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(thumbnailDownloader)
    }





    private class PhotoHolder(itemImageView: ImageView): RecyclerView.ViewHolder(itemImageView) {
        val bindDrawable:(Drawable)->Unit = itemImageView::setImageDrawable

    }

    private inner class PhotoAdapter(private val galleryItems:List<GalleryItem>): RecyclerView.Adapter<PhotoHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
            val view = layoutInflater.inflate(R.layout.photo_item,parent,false) as ImageView
            return PhotoHolder(view)
        }

        override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
            val galleryItem = galleryItems[position]
            val placeholder:Drawable = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.diamond
            )?: ColorDrawable()
            holder.bindDrawable(placeholder)
            thumbnailDownloader.queueThumbnail(holder,galleryItem.src)

        }

        override fun getItemCount(): Int {
            return galleryItems.size
        }

    }


    companion object {
        fun newInstance(): NasaPhotosFragment {
            return NasaPhotosFragment()
        }
    }
}