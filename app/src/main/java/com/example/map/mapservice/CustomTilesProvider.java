package com.example.map.mapservice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.LruCache;

import com.example.map.model.Tile;

import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.util.MapTileIndex;

import java.util.Set;


public class CustomTilesProvider extends MapTileProviderBasic {

    private final Set<Tile> availableTiles; // Danh sách các tile hợp lệ từ backend
    private final LruCache<String, Bitmap> tileCache;

    public CustomTilesProvider(Context context, ITileSource tileSource, Set<Tile> tiles) {
        super(context);
        this.availableTiles = tiles;
        this.setTileSource(tileSource); // Gán tile source

        int cacheSize = 50 * 256 * 256; // 50MB
        tileCache = new LruCache<>(cacheSize);
    }

    public boolean shouldTileBeLoaded(int zoomLevel, int x, int y) {
        // Tạo khóa tile dưới dạng "z-x-y"
        String tileKey = zoomLevel + "-" + x + "-" + y;
        return availableTiles.contains(tileKey); // Chỉ tải tile nếu hợp lệ
    }


    @Override
    public Drawable getMapTile(long pMapTileIndex) {
        int zoomLevel = MapTileIndex.getZoom(pMapTileIndex);
        int tileColumn = MapTileIndex.getX(pMapTileIndex);
        int tileRow = MapTileIndex.getY(pMapTileIndex);
        String tileKey = zoomLevel + "-" + tileColumn + "-" + tileRow;

        if (availableTiles.contains(tileKey)) {
            return super.getMapTile(pMapTileIndex);  // Lấy tile nếu nó có trong availableTiles
        } else {
            // Nếu không có trong availableTiles, trả về null hoặc một tile thay thế
            return null;
        }
    }
}





