package com.subastas.virtual.service;

import com.subastas.virtual.dto.item.ItemPhoto;
import com.subastas.virtual.dto.item.RegisteredItem;
import com.subastas.virtual.dto.item.http.request.RegisterItemRequest;
import com.subastas.virtual.exception.custom.NotFoundException;
import com.subastas.virtual.repository.ItemPhotoRepository;
import com.subastas.virtual.repository.ItemRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemPhotoRepository itemPhotoRepository;

    public ItemService(ItemRepository itemRepository, ItemPhotoRepository itemPhotoRepository) {
        this.itemRepository = itemRepository;
        this.itemPhotoRepository = itemPhotoRepository;
    }

    public RegisteredItem registerItem(RegisterItemRequest request) {
        return itemRepository.save(new RegisteredItem(request));
    }

    @SneakyThrows
    public RegisteredItem attachPicture(int itemId, MultipartFile picture) {
        RegisteredItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("item", itemId));

        ItemPhoto dbImage = new ItemPhoto();
        dbImage.setName(picture.getName());
        dbImage.setContent(picture.getBytes());

        itemPhotoRepository.save(dbImage);

        List<Integer> existingPhotos = item.getPhotoIds();
        existingPhotos.add(dbImage.getId());
        item.setPhotoIds(existingPhotos);
        itemRepository.save(item);

        return item;
    }

    public RegisteredItem getItem(int itemId) {
        return itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException("item", itemId)
        );
    }

    public Object getItemPhoto(int itemId, int photoId) {
        List<Integer> photos = getItem(itemId).getPhotoIds();

        ItemPhoto photo = itemPhotoRepository.findById(photoId).orElseThrow(() -> new NotFoundException("photo", photoId));

        return photo.getContent();
    }
}
