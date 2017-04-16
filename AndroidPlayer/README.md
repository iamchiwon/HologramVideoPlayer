## Android Player

- 비디오 회전시키기

VideoView 는 flip이나 회전이 적용이 안되기 때문에, TextureView 의 Surface에 비디오를 그리도록 하고 TextureView 를 회전시키도록 한다.

```java
public class FlippableVideoView extends TextureView implements TextureView.SurfaceTextureListener {

    private MediaPlayer mediaPlayer;

    private void init() {
        mediaPlayer = new MediaPlayer();
        setSurfaceTextureListener(this);
    }

    public void startVideo(Context context, Uri videoUri) {
        try {
            mediaPlayer.setDataSource(context, videoUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rotateRight() {
        this.setRotation(90.0f);
    }

    public void rotateLeft() {
        this.setRotation(-90.0f);
    }

    public void flipH() {
        this.setScaleX(-1 * this.getScaleX());
    }

    public void flipV() {
        this.setScaleY(-1 * this.getScaleY());
    }


    /****
     * Implements of SurfaceTextureListener
     */

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        mediaPlayer.setSurface(new Surface(surfaceTexture));
    }
}
```
