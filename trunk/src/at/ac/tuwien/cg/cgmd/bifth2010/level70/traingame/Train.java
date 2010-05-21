package at.ac.tuwien.cg.cgmd.bifth2010.level70.traingame;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.geometry.Geometry;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.geometry.GeometryFactory;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.geometry.SpriteTexture;

/**
 * This class manages the train including it's wagons moving along the rails.
 * The game is over if a train can not move to the next tile and completed if
 * there is a connection to the goal tile.
 * 
 * @author Christoph Winklhofer.
 */
public class Train {

    private static String STATE_TRAIN_INDEX = "StateTrainIndex";
    private static String STATE_TRAIN_POS = "StateTrainPos";
    private static String STATE_TRAIN_ANGLE = "StateTrainAngle";
    private static String STATE_TRAIN_DIR = "StateTrainDir";
    private static String STATE_TRAIN_TIME = "StateTrainTime";
    public static final float TILE_SIZE = 1.0f;
    private static float SPRITE_ANIM_TIME = 0.5f;

    /**
     * Wagon data.
     */
    private class Wagon {
        Wagon(int ixOff) {
            ix = ixTile - ixOff;
            iy = iyTile;
            posX = -5.0f + ix * Tile.TILE_SIZE;
            posY = -2.5f + iy * Tile.TILE_SIZE;
            offX = offY = 0.0f;
            angleOrient = angleOffset = 0.0f;
            totalDt = 0.0f;
            type = TileEnum.TILE_HORIZONTAL;
            dir = 1;
        }
        
                
        Geometry geom;
        float posX, posY;
        float offX, offY;
        float angleOrient;
        float angleOffset;
        float totalDt;
        int ix, iy;
        TileEnum type;
        int dir;
    };
    
    // ----------------------------------------------------------------------------------
    // -- Members ----

    private TrainGame game;  //< Reference to the game

    private SpriteTexture tex;   //< The train sprite texture
    private Geometry      geom;  //< Train quad-geometry
    private ArrayList<Wagon> wagons;

    private Tile     tile;  //< Current tile
    private TileEnum type;  //< Current type of the tile

    private int   dir;             //< Moving direction of the train
    private int   ixTile, iyTile;  //< X and Y index of the current tile
    private float posX, posY;      //< World position of the train
    private float offX, offY;      //< Offset position of the train
    private float angleOrient;     //< World orientation of the train
    private float angleOffset;     //< Offset orientation of the train

    private int iSprite;
    private float spriteTime;

    private float tileTime;  //< Time the train needs to move along one tile
    private float totalDt;   //< Total time inside one tile.
    
    
    // ----------------------------------------------------------------------------------
    // -- Ctor ----

    /**
     * Constructor of the train.
     * 
     * @param game
     *            Reference to the train game.
     * @param tileTime
     *            The time the train needs to move along one tile. (Speed of the
     *            train)
     */
    public Train(TrainGame game, float tileTime, int ix, int iy,
            float startTime) {
        this.game = game;
        this.tileTime = tileTime;
        this.ixTile = ix;
        this.iyTile = iy;

        // Calculate world position
        posX = -5.0f + ixTile * Tile.TILE_SIZE;
        posY = -2.5f + iyTile * Tile.TILE_SIZE;

        iSprite = 0;
        spriteTime = SPRITE_ANIM_TIME;

        tile = null;
        type = TileEnum.TILE_HORIZONTAL;
        totalDt = startTime;
        angleOrient = 0.0f;
        dir = 1;
        
        // Create 3 wagons
        wagons = new ArrayList<Wagon>(3);
        for (int i = 1; i <= 3; i++) {
            Wagon wagon = new Wagon(i);
            wagon.totalDt = i / 4.0f * tileTime;
            wagons.add(wagon);
        }
    }

    
    // ----------------------------------------------------------------------------------
    // -- Public methods ----

    /**
     * Create the OpenGl specific data for the train. The train is represented
     * by a simple quad and sprite texture coordinates.
     * 
     * @param tex
     *            The sprite texture map for the train.
     */
    public void createOpenGl(SpriteTexture tex) {
        this.tex = tex;
        geom = GeometryFactory.createQuad(0, 0, TILE_SIZE, TILE_SIZE);
        geom.setTexBuffer(tex.getTexBuffer(iSprite));
        
        // Set OpenGl data for the wagons
        for (Wagon it : wagons) {
            it.geom = GeometryFactory.createQuad(0, 0, TILE_SIZE, TILE_SIZE);
            it.geom.setTexBuffer(tex.getTexBuffer(4));
        }
    }
    

    public void setTileTime(float time) {
        tileTime = time;
        //update(0);
    }


    public void onSaveState(Bundle state) {
        int[] inds = new int[4 * 2];
        int[] dirs = new int[4];
        float[] poss = new float[4 * 2];
        float[] angles = new float[4];
        float[] times = new float[4];
        
        inds[0]   = ixTile;  
        inds[1]   = iyTile;
        poss[0]   = posX;    
        poss[1]   = posY;
        angles[0] = angleOrient;
        dirs[0]   = dir;
        times[0]  = totalDt;
        
        for (int i = 0; i < wagons.size(); i++) {
            int iw = i + 1;
            Wagon wag = wagons.get(i);
            inds[iw * 2] = wag.ix;     
            inds[iw * 2 + 1] = wag.iy;
            poss[iw * 2]  = wag.posX;   
            poss[iw * 2 + 1] = wag.posY;
            angles[iw]   = wag.angleOrient;
            dirs[iw]     = wag.dir;
            times[iw]    = wag.totalDt;
        }
        
        state.putIntArray(STATE_TRAIN_INDEX, inds);
        state.putIntArray(STATE_TRAIN_DIR, dirs);
        state.putFloatArray(STATE_TRAIN_POS, poss);
        state.putFloatArray(STATE_TRAIN_ANGLE, angles);
        state.putFloatArray(STATE_TRAIN_TIME, times);
    }
    
    public void onRestoreState(Bundle state) {
        int[]   stateInds  = state.getIntArray(STATE_TRAIN_INDEX);
        int[]   stateDirs  = state.getIntArray(STATE_TRAIN_DIR);
        float[] stateTimes = state.getFloatArray(STATE_TRAIN_TIME);
        float[] statePoss = state.getFloatArray(STATE_TRAIN_POS);
        float[] stateAngles = state.getFloatArray(STATE_TRAIN_ANGLE);

        if (stateInds != null && stateDirs != null && stateTimes != null) {
            ixTile  = stateInds[0];
            iyTile  = stateInds[1];
            posX    = statePoss[0];
            posY    = statePoss[1];
            angleOrient = stateAngles[0];
            dir     = stateDirs[0];
            totalDt = stateTimes[0];
            
            tile = game.getTile(ixTile, iyTile);
            type = tile.getType();
            
            for (int i = 0; i < wagons.size(); i++) {
                int iw = i + 1;
                Wagon wag = wagons.get(i);
                wag.ix      = stateInds[iw * 2];
                wag.iy      = stateInds[iw * 2 + 1];
                wag.posX    = statePoss[iw * 2];
                wag.posY    = statePoss[iw * 2 + 1];
                wag.dir     = stateDirs[iw];
                wag.angleOrient = stateAngles[iw];
                wag.totalDt = stateTimes[iw];
                wag.type    = game.getTile(wag.ix, wag.iy).getType();
            }
        }
    }
    
    
    /**
     * Checks if there is a direct connection from the train to the goal tile.
     * 
     * @return true if the game is completed, otherwise false.
     */
    public boolean isGoalReached() {

        // Save current train data, because the update-methods alter these values.
        Tile saveTile = tile;
        int saveIxTile = ixTile;
        int saveIyTile = iyTile;
        int saveDir = dir;
        float savePosX = posX;
        float savePosY = posY;
        float saveAngle = angleOrient;

        // Move along the rail until we reach an invalid tile or the goal.
        boolean isFinished = false;
        while (true) {
            updateIndexAndPosition();

            // Outside the playfield
            if (ixTile < 0 || iyTile < 0 || ixTile > 9 || iyTile > 5) {
                break;
            }

            // Get next tile
            Tile ntile = game.getTile(ixTile, iyTile);
            boolean isOk = updateDirection(ntile);
            if (!isOk) {
                break;
            }

            // There is a direct connection to the goal
            if (ntile.isGoal()) {
                isFinished = true;
                break;
            }
            tile = ntile;
            type = ntile.getType();
        }

        // Restore the original tile data
        if (saveTile != null) {
            tile = saveTile;
            type = saveTile.getType();
        }
        ixTile = saveIxTile;
        iyTile = saveIyTile;
        dir = saveDir;
        posX = savePosX;
        posY = savePosY;
        angleOrient = saveAngle;

        return isFinished;
    }

    /**
     * Update the train.
     * 
     * @param dt
     *            The delta time
     */
    public void update(float dt) {
        
        if (!game.isGameOver()) {
            if (totalDt + dt >= tileTime) {
                updateTrain();
            } 
            else {
                updateOffset(dt);
            }
            
            if (isGoalReached()) {
                game.onGameCompleted();
            }
        }
        
        if (spriteTime <= 0) {
            ++iSprite;
            if (iSprite > 2) {
                iSprite = 0;
            }
            geom.setTexBuffer(tex.getTexBuffer(iSprite));
            spriteTime = SPRITE_ANIM_TIME;
        }
        else {
            spriteTime -= dt;
        }
    }

    /**
     * Draw the train.
     * 
     * @param gl
     *            The OpenGl context.
     */
    public void draw(GL10 gl) {

        gl.glPushMatrix();
        gl.glTranslatef(posX + offX, posY + offY, 0);
        gl.glRotatef(angleOrient + angleOffset, 0, 0, 1);
        geom.draw(gl);
        gl.glPopMatrix();
        
        for (Wagon it : wagons) {
            gl.glPushMatrix();
            gl.glTranslatef(it.posX + it.offX, it.posY + it.offY, 0);
            gl.glRotatef(it.angleOrient + it.angleOffset, 0, 0, 1);
            it.geom.draw(gl);
            gl.glPopMatrix();
        }
    }

    
    // ----------------------------------------------------------------------------------
    // -- Private methods ----

    /**
     * Update the train. Update the dynamic parameters for the train, that is
     * setting the next tile indices, the world position and the moving
     * direction. In addition set a "game over" state, if the train can not move
     * to the next tile, for example horizontal rail tile followed by a vertical
     * rail tile.
     */
    private void updateTrain() {

        // Reset offset and total time
        offX = offY = 0;
        angleOffset = 0;
        totalDt = 0.0f;
        
        // Update tile index and train world position.
        updateIndexAndPosition();
        
        // Update wagon offset time, which is relative to the train
        Wagon wag = wagons.get(0);
        wag.totalDt = 1.0f / 4.0f * tileTime;
        wag = wagons.get(1);
        wag.totalDt = 2.0f / 4.0f * tileTime;
        wag = wagons.get(2);
        wag.totalDt = 3.0f / 4.0f * tileTime;

        // The train has not entered the playfield
        if (tile == null && ixTile < 0) {
            return;
        }
                
        // Train moves outside the playfield - game over
        if (ixTile < 0 || iyTile < 0 || ixTile > 9 || iyTile > 5) {
            if (!game.isGameCompleted()) {
                geom.setTexBuffer(tex.getTexBuffer(iSprite));
                game.onGameOver();
                return;
            }
        }
        
        // Get next tile and update its state (Tile could be an Switch-state)
        Tile ntile = game.getTile(ixTile, iyTile);
        ntile.setStateTrain();

        // Update direction and set next tile.
        boolean isOk = updateDirection(ntile);
        if (isOk) {           
            tile = ntile;
            type = ntile.getType();
        } 
        else {
            geom.setTexBuffer(tex.getTexBuffer(iSprite));
            game.onGameOver();
        }
    }

    /**
     * Update tile index and world position according to the actual tile and
     * moving direction.
     */
    private void updateIndexAndPosition() {

        if (type == TileEnum.TILE_HORIZONTAL) {
            if (dir > 0) {
                ixTile += 1;
                posX += 1.0f;
            } 
            else {
                ixTile -= 1;
                posX -= 1.0f;
            }
        } 
        else if (type == TileEnum.TILE_VERTICAL) {
            if (dir > 0) {
                iyTile += 1;
                posY += 1.0f;
            } 
            else {
                iyTile -= 1;
                posY -= 1.0f;
            }
        } 
        else if (type == TileEnum.TILE_LEFT_DOWN) {
            if (dir > 0) {
                ixTile -= 1;
                posX -= 0.5f;
                posY += 0.5f;
                angleOrient += 90;
            }
            else {
                iyTile -= 1;
                posX += 0.5f;
                posY -= 0.5f;
                angleOrient -= 90;
            }
        } 
        else if (type == TileEnum.TILE_LEFT_UP) {
            if (dir > 0) {
                iyTile += 1;
                posX += 0.5f;
                posY += 0.5f;
                angleOrient += 90;
            }
            else {
                ixTile -= 1;
                posX -= 0.5f;
                posY -= 0.5f;
                angleOrient -= 90;
            }
        } 
        else if (type == TileEnum.TILE_RIGHT_DOWN) {
            if (dir > 0) {
                iyTile -= 1;
                posX -= 0.5f;
                posY -= 0.5f;
                angleOrient += 90;
            }
            else {
                ixTile += 1;
                posX += 0.5f;
                posY += 0.5f;
                angleOrient -= 90;
            }
        } 
        else if (type == TileEnum.TILE_RIGHT_UP) {
            if (dir > 0) {
                ixTile += 1;
                posX += 0.5f;
                posY -= 0.5f;
                angleOrient += 90;
            } 
            else {
                iyTile += 1;
                posX -= 0.5f;
                posY += 0.5f;
                angleOrient -= 90;
            }
        }
    }

    /**
     * Update the direction and return the next valid tile.
     * 
     * @return Tile The next valid tile or null if the train can not move to the
     *         next tile.
     */
    public boolean updateDirection(Tile ntile) {

        TileEnum ntype = ntile.getType();

        if (dir > 0 && type == TileEnum.TILE_HORIZONTAL) {
            if (ntype == TileEnum.TILE_HORIZONTAL) {
                dir = 1;
            } 
            else if (ntype == TileEnum.TILE_LEFT_DOWN) {
                dir = -1;
            } 
            else if (ntype == TileEnum.TILE_LEFT_UP) {
                dir = 1;
            } 
            else {
                return false;
            }
        } 
        else if (dir < 0 && type == TileEnum.TILE_HORIZONTAL) {
            if (ntype == TileEnum.TILE_HORIZONTAL) {
                dir = -1;
            } 
            else if (ntype == TileEnum.TILE_RIGHT_DOWN) {
                dir = 1;
            } 
            else if (ntype == TileEnum.TILE_RIGHT_UP) {
                dir = -1;
            } 
            else {
                return false;
            }
        }

        else if (dir > 0 && type == TileEnum.TILE_VERTICAL) {
            if (ntype == TileEnum.TILE_VERTICAL) {
                dir = 1;
            } 
            else if (ntype == TileEnum.TILE_RIGHT_DOWN) {
                dir = -1;
            } 
            else if (ntype == TileEnum.TILE_LEFT_DOWN) {
                dir = 1;
            } 
            else {
                return false;
            }
        }
        else if (dir < 0 && type == TileEnum.TILE_VERTICAL) {
            if (ntype == TileEnum.TILE_VERTICAL) {
                dir = -1;
            } 
            else if (ntype == TileEnum.TILE_RIGHT_UP) {
                dir = 1;
            } 
            else if (ntype == TileEnum.TILE_LEFT_UP) {
                dir = -1;
            } 
            else {
                return false;
            }
        }

        else if (dir > 0 && type == TileEnum.TILE_LEFT_DOWN) {
            if (ntype == TileEnum.TILE_HORIZONTAL) {
                dir = -1;
            } 
            else if (ntype == TileEnum.TILE_RIGHT_UP) {
                dir = -1;
            } 
            else if (ntype == TileEnum.TILE_RIGHT_DOWN) {
                dir = 1;
            } 
            else {
                return false;
            }
        } 
        else if (dir < 0 && type == TileEnum.TILE_LEFT_DOWN) {
            if (ntype == TileEnum.TILE_VERTICAL) {
                dir = -1;
            } 
            else if (ntype == TileEnum.TILE_RIGHT_UP) {
                dir = 1;
            } 
            else if (ntype == TileEnum.TILE_LEFT_UP) {
                dir = -1;
            } 
            else {
                return false;
            }
        }

        else if (dir > 0 && type == TileEnum.TILE_LEFT_UP) {
            if (ntype == TileEnum.TILE_VERTICAL) {
                dir = 1;
            } 
            else if (ntype == TileEnum.TILE_LEFT_DOWN) {
                dir = 1;
            } 
            else if (ntype == TileEnum.TILE_RIGHT_DOWN) {
                dir = -1;
            } 
            else {
                return false;
            }
        } 
        else if (dir < 0 && type == TileEnum.TILE_LEFT_UP) {
            if (ntype == TileEnum.TILE_HORIZONTAL) {
                dir = -1;
            } 
            else if (ntype == TileEnum.TILE_RIGHT_DOWN) {
                dir = 1;
            } 
            else if (ntype == TileEnum.TILE_RIGHT_UP) {
                dir = -1;
            } 
            else {
                return false;
            }
        }

        else if (dir > 0 && type == TileEnum.TILE_RIGHT_DOWN) {
            if (ntype == TileEnum.TILE_VERTICAL) {
                dir = -1;
            } 
            else if (ntype == TileEnum.TILE_LEFT_UP) {
                dir = -1;
            }
            else if (ntype == TileEnum.TILE_RIGHT_UP) {
                dir = 1;
            } 
            else {
                return false;
            }
        } 
        else if (dir < 0 && type == TileEnum.TILE_RIGHT_DOWN) {
            if (ntype == TileEnum.TILE_HORIZONTAL) {
                dir = 1;
            } 
            else if (ntype == TileEnum.TILE_LEFT_UP) {
                dir = 1;
            } 
            else if (ntype == TileEnum.TILE_LEFT_DOWN) {
                dir = -1;
            } 
            else {
                return false;
            }
        }

        else if (dir > 0 && type == TileEnum.TILE_RIGHT_UP) {
            if (ntype == TileEnum.TILE_HORIZONTAL) {
                dir = 1;
            } 
            else if (ntype == TileEnum.TILE_LEFT_UP) {
                dir = 1;
            } 
            else if (ntype == TileEnum.TILE_LEFT_DOWN) {
                dir = -1;
            } 
            else {
                return false;
            }
        } 
        else if (dir < 0 && type == TileEnum.TILE_RIGHT_UP) {
            if (ntype == TileEnum.TILE_VERTICAL) {
                dir = 1;
            } 
            else if (ntype == TileEnum.TILE_LEFT_DOWN) {
                dir = 1;
            } 
            else if (ntype == TileEnum.TILE_RIGHT_DOWN) {
                dir = -1;
            } 
            else {
                return false;
            }
        }

        return true;
    }

    /**
     * Update the offset position of the train according to the actual tile type
     * and the moving direction.
     */
    private void updateOffset(float dt) {

        totalDt += dt;
        
        float offs[] = getOffsets(type, dir, totalDt);
        offX         = offs[0];
        offY         = offs[1];
        angleOffset  = offs[2];
        
        // Update wagons
        for (int i = 0; i < wagons.size(); i++) {
            Wagon wag = wagons.get(i);
            wag.totalDt += dt;
            if (wag.totalDt > tileTime) {
                
                if (i == 0) {
                    wag.totalDt = 0;
                    wag.ix   = ixTile;
                    wag.iy   = iyTile;
                    wag.posX = posX;
                    wag.posY = posY;
                    wag.angleOrient = angleOrient;
                    wag.type = type;
                    wag.dir  = dir;
                }
                else {
                    Wagon last = wagons.get(i - 1);
                    wag.totalDt = 0;
                    wag.ix   = last.ix;
                    wag.iy   = last.iy;
                    wag.posX = last.posX;
                    wag.posY = last.posY;
                    wag.angleOrient = last.angleOrient;
                    wag.type = last.type;
                    wag.dir  = last.dir;
                }
            }
            float wagOffs[] = getOffsets(wag.type, wag.dir, wag.totalDt);
            wag.offX = wagOffs[0];
            wag.offY = wagOffs[1];
            wag.angleOffset = wagOffs[2];
        }
    }
    
    
    /**
     * Return the offset positions and offset orientation of the train according 
     * to the tile type and moving direction.
     * @param type The tile type.
     * @param dir The moving direction.
     * @param time The total time the train or wagon has already moved onto the tile.
     * @return Return the offsets. 
     *          First array element is offset in x position.
     *          Second array element is offset in y position.
     *          Third array element is offset orientation. 
     *          
     */
    private float[] getOffsets(TileEnum type, int dir, float time) {

        float[] offs = new float[3];
        float tf = time / tileTime * dir;
        
        if (type == TileEnum.TILE_HORIZONTAL) {
            offs[0] = tf;
            offs[1] = 0.0f;
            offs[2] = 0.0f;
        } 
        else if (type == TileEnum.TILE_VERTICAL) {
            offs[0] = 0.0f;
            offs[1] = tf;
            offs[2] = 0.0f;
        } 
        else if (type == TileEnum.TILE_LEFT_DOWN) {
            offs[2] = 90.0f * tf;
            float sina = (float) Math.sin(Math.toRadians(offs[2]));
            float cosa = (float) Math.cos(Math.toRadians(offs[2]));
            if (dir > 0) {
                offs[0] = -0.5f + cosa * 0.5f;
                offs[1] = sina * 0.5f;
            } 
            else {
                offs[0] = -sina * 0.5f;
                offs[1] = -0.5f + cosa * 0.5f;
            }
        } 
        else if (type == TileEnum.TILE_RIGHT_DOWN) {
            offs[2] = 90.0f * tf;
            float sina = (float) Math.sin(Math.toRadians(offs[2]));
            float cosa = (float) Math.cos(Math.toRadians(offs[2]));
            if (dir > 0) {
                offs[0] = -sina * 0.5f;
                offs[1] = -0.5f + cosa * 0.5f;
            } 
            else {
                offs[0] = 0.5f - cosa * 0.5f;
                offs[1] = -sina * 0.5f;
            }
        } 
        else if (type == TileEnum.TILE_RIGHT_UP) {
            offs[2] = 90.0f * tf;
            float sina = (float) Math.sin(Math.toRadians(offs[2]));
            float cosa = (float) Math.cos(Math.toRadians(offs[2]));
            if (dir > 0) {
                offs[0] = 0.5f - cosa * 0.5f;
                offs[1] = -sina * 0.5f;
            } 
            else {
                offs[0] = sina * 0.5f;
                offs[1] = 0.5f - cosa * 0.5f;
            }
        } 
        else if (type == TileEnum.TILE_LEFT_UP) {
            offs[2] = 90.0f * tf;
            float sina = (float) Math.sin(Math.toRadians(offs[2]));
            float cosa = (float) Math.cos(Math.toRadians(offs[2]));
            if (dir > 0) {
                offs[0] = sina * 0.5f;
                offs[1] = 0.5f - cosa * 0.5f;
            } 
            else {
                offs[0] = -0.5f + cosa * 0.5f;
                offs[1] = sina * 0.5f;
            }
        }
        
        return offs;
    }
}
