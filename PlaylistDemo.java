import java.util.Arrays;

public class PlaylistDemo {

    private static final int MAXIMUM_SONGS = 10;
    private static final String[] DEMO_SONGS = { "Song A", "Song B" };

    public static void main(String[] args) {
        Playlist playlist = new Playlist(MAXIMUM_SONGS);

        for (int songIndex = 0; songIndex < DEMO_SONGS.length; songIndex++) {
            playlist.addSong(DEMO_SONGS[songIndex]);
            System.out.println("addSong(\"" + DEMO_SONGS[songIndex] + "\") -> count = " + playlist.getSongCount());
        }

        String[] songsCopy = playlist.getSongs();
        System.out.println("getSongs() -> " + Arrays.toString(songsCopy));

        songsCopy[0] = "Hacked";
        System.out.println("copy[0] changed to \"" + songsCopy[0] + "\"");
        System.out.println("getSongs()[0] is still \"" + playlist.getSongs()[0] + "\"");
        System.out.println("getSongCount() -> " + playlist.getSongCount());
    }
}

class Playlist {

    private final String[] songs;
    private int songCount;

    Playlist(int maximumSongs) {
        this.songs = new String[maximumSongs];
        this.songCount = 0;
    }

    boolean addSong(String title) {
        if (this.songCount >= this.songs.length) {
            return false;
        }
        this.songs[this.songCount] = title;
        this.songCount++;
        return true;
    }

    String[] getSongs() {
        return Arrays.copyOf(this.songs, this.songCount);
    }

    int getSongCount() {
        return this.songCount;
    }
}
