package Entities;

import Data.NPC;

import java.awt.*;
import java.awt.image.BufferedImage;

import static Engine.Game.SCALE;

/**
 * Represents a concrete NPC graphical entity on the station map layout.
 * Binds the theoretical data configuration container with sprite visualization.
 * @author Ondřej Ptáček
 */
public class NPCEntity extends Entity {

    private NPC npc;

    private BufferedImage sprite;

    public static final int NPC_SIZE = 96 * (int) SCALE;

    /**
     * Initializes a new graphical NPC wrapper entity instance.
     * @param x global horizontal position layout coordinate
     * @param y global vertical position layout coordinate
     * @param npc core data properties reference configuration
     * @param sprite source image representation asset for drawing
     */
    public NPCEntity(float x, float y, NPC npc, BufferedImage sprite) {
        super(x, y);

        this.npc = npc;

        this.sprite = sprite;
    }

    /**
     * Lifecycle frame tick update logic method.
     * Reserved for future idle or movement animations.
     */
    @Override
    public void update() {
        // zatím nic
        // později:
        // idle animace
    }

    /**
     * Renders the NPC onto the current graphics context panel container.
     * Uses a yellow bounding box indicator fallback if the source sprite is null.
     * @param g current Graphics context container instance
     */
    @Override
    public void render(Graphics g) {

        // fallback debug render
        if (sprite == null) {

            Graphics2D g2d = (Graphics2D) g;

            g2d.setColor(Color.YELLOW);
            g2d.fillRect((int)x, (int)y, 48, 48);

            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Courier New", Font.BOLD, 12));

            g2d.drawString(npc.getName(), (int)x - 10, (int)y - 10);

            return;
        }

        g.drawImage(sprite, (int)x, (int)y, NPC_SIZE, NPC_SIZE, null);
    }

    public NPC getNpc() {
        return npc;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }
}