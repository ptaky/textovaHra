package Entities;

import Data.NPC;

import java.awt.*;
import java.awt.image.BufferedImage;

import static Engine.Game.SCALE;

public class NPCEntity extends Entity {

    private NPC npc;

    private BufferedImage sprite;

    public static final int NPC_SIZE = 96 * (int) SCALE;

    public NPCEntity(float x, float y, NPC npc, BufferedImage sprite) {
        super(x, y);

        this.npc = npc;

        this.sprite = sprite;
    }

    @Override
    public void update() {

        // zatím nic
        // později:
        // idle animace
    }

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