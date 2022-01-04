package net.minestom.server.instance;

import net.minestom.server.instance.palette.Palette;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PaletteTest {

    @Test
    public void placement() {
        var palettes = testPalettes();
        for (Palette palette : palettes) {
            final int dimension = palette.dimension();
            assertEquals(0, palette.get(0, 0, 0), "Default value should be 0");
            assertEquals(0, palette.size());
            palette.set(0, 0, 0, 64);
            assertEquals(64, palette.get(0, 0, 0));
            assertEquals(64, palette.get(dimension, 0, 0), "Coordinate must be rounded to the palette dimension");
            assertEquals(1, palette.size());

            palette.set(1, 0, 0, 65);
            assertEquals(64, palette.get(0, 0, 0));
            assertEquals(65, palette.get(1, 0, 0));
            assertEquals(2, palette.size());

            palette.set(0, 1, 0, 66);
            assertEquals(64, palette.get(0, 0, 0));
            assertEquals(65, palette.get(1, 0, 0));
            assertEquals(66, palette.get(0, 1, 0));
            assertEquals(3, palette.size());

            palette.set(0, 0, 1, 67);
            assertEquals(64, palette.get(0, 0, 0));
            assertEquals(65, palette.get(1, 0, 0));
            assertEquals(66, palette.get(0, 1, 0));
            assertEquals(67, palette.get(0, 0, 1));
            assertEquals(4, palette.size());

            palette.set(0, 0, 1, 68);
            assertEquals(4, palette.size());
        }
    }

    @Test
    public void negPlacement() {
        var palettes = testPalettes();
        for (Palette palette : palettes) {
            assertThrows(IllegalArgumentException.class, () -> palette.set(-1, 0, 0, 64));
            assertThrows(IllegalArgumentException.class, () -> palette.set(0, -1, 0, 64));
            assertThrows(IllegalArgumentException.class, () -> palette.set(0, 0, -1, 64));

            assertThrows(IllegalArgumentException.class, () -> palette.get(-1, 0, 0));
            assertThrows(IllegalArgumentException.class, () -> palette.get(0, -1, 0));
            assertThrows(IllegalArgumentException.class, () -> palette.get(0, 0, -1));
        }
    }

    @Test
    public void resize() {
        Palette palette = Palette.newPalette(16, 5, 2, 1);
        palette.set(0, 0, 0, 1);
        assertEquals(2, palette.bitsPerEntry());
        palette.set(0, 0, 1, 2);
        assertEquals(2, palette.bitsPerEntry());
        palette.set(0, 0, 2, 3);
        assertEquals(2, palette.bitsPerEntry());

        palette.set(0, 0, 3, 4);
        assertEquals(3, palette.bitsPerEntry());
        assertEquals(1, palette.get(0, 0, 0));
        assertEquals(2, palette.get(0, 0, 1));
        assertEquals(3, palette.get(0, 0, 2));
        assertEquals(4, palette.get(0, 0, 3));
    }


    @Test
    public void fill() {
        var palettes = testPalettes();
        for (Palette palette : palettes) {
            assertEquals(0, palette.size());
            palette.set(0, 0, 0, 5);
            assertEquals(1, palette.size());
            assertEquals(5, palette.get(0, 0, 0));
            palette.fill(6);
            assertEquals(6, palette.get(0, 0, 0));
            assertEquals(palette.maxSize(), palette.size());
            for (int y = 0; y < palette.dimension(); y++) {
                for (int x = 0; x < palette.dimension(); x++) {
                    for (int z = 0; z < palette.dimension(); z++) {
                        assertEquals(6, palette.get(x, y, z));
                    }
                }
            }

            palette.fill(0);
            assertEquals(0, palette.size());
            for (int y = 0; y < palette.dimension(); y++) {
                for (int x = 0; x < palette.dimension(); x++) {
                    for (int z = 0; z < palette.dimension(); z++) {
                        assertEquals(0, palette.get(x, y, z));
                    }
                }
            }
        }
    }

    @Test
    public void dimension() {
        assertThrows(Exception.class, () -> Palette.newPalette(-4, 5, 3, 1));
        assertThrows(Exception.class, () -> Palette.newPalette(0, 5, 3, 1));
        assertThrows(Exception.class, () -> Palette.newPalette(1, 5, 3, 1));
        assertDoesNotThrow(() -> Palette.newPalette(2, 5, 3, 1));
        assertThrows(Exception.class, () -> Palette.newPalette(3, 5, 3, 1));
        assertDoesNotThrow(() -> Palette.newPalette(4, 5, 3, 1));
        assertThrows(Exception.class, () -> Palette.newPalette(6, 5, 3, 1));
        assertDoesNotThrow(() -> Palette.newPalette(16, 5, 3, 1));
    }

    private static List<Palette> testPalettes() {
        List<Palette> palettes = new ArrayList<>();
        palettes.add(Palette.newPalette(2, 5, 3, 1));
        palettes.add(Palette.newPalette(4, 5, 3, 1));
        palettes.add(Palette.newPalette(8, 5, 3, 1));
        palettes.add(Palette.newPalette(16, 5, 3, 1));
        return palettes;
    }

    @Test
    public void bulk() {
        var palettes = testPalettes();
        for (Palette palette : palettes) {
            final int dimension = palette.dimension();
            // Place
            for (int x = 0; x < dimension; x++) {
                for (int y = 0; y < dimension; y++) {
                    for (int z = 0; z < dimension; z++) {
                        palette.set(x, y, z, x + y + z);
                    }
                }
            }
            // Verify
            for (int x = 0; x < dimension; x++) {
                for (int y = 0; y < dimension; y++) {
                    for (int z = 0; z < dimension; z++) {
                        assertEquals(x + y + z, palette.get(x, y, z));
                    }
                }
            }
        }
    }
}