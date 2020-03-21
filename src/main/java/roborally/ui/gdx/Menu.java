package roborally.ui.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import roborally.game.IGame;
import roborally.ui.gdx.events.Events;
import roborally.utilities.AssetManagerUtil;

import java.util.ArrayList;
import java.util.HashMap;

// Menu under construction!
public class Menu {
    public static float dt = 0.00000000000001f;
    private ArrayList<Slider> sliders;
    private Slider.SliderStyle sliderStyle1;
    private float[] decoIterate;
    private int[] decoScale;
    private float[] decoWidth;
    private float superit;
    private float superit2;
    private boolean flip1;
    private boolean flip2;
    private boolean decoH1;
    private boolean decoH2;
    private boolean resume;
    private boolean flip3;
    private boolean decoH3;
    private boolean changeMap;
    private Music Song;
    private int mapId;
    private int startGame;
    private HashMap<String, ArrayList<Image>> imageLists;
    private boolean changeMapMenu;
    private Label playSong;
    private Label funMode;
    private Label left;
    private Label right;
    private Label gameSpeed;
    private Label laserSpeed;
    private Label volume;
    private Slider volumeSlider;
    private Label.LabelStyle labelStyle;
    private Image mapButton;
    private Image continueButton;
    private Events events;
    private int gSpeed;
    private int lSpeed;
    private int fun;
    private boolean isFunMode;
    private boolean Continue;

    public Menu(Stage stage, Events events) {
        labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        this.decoIterate = new float[3];
        this.decoScale = new int[3];
        this.decoWidth = new float[3];
        this.resume = false;
        this.changeMap = false;
        this.mapId = 1;
        this.fun = 0;
        this.superit = 0;
        this.events = events;
        this.sliders = new ArrayList<>();
        imageLists = new HashMap<>();
        startGame = 0;
        imageLists.put("menus", new ArrayList<>());
        Image menu = new Image(AssetManagerUtil.getMenu());
        menu.setSize(stage.getViewport().getScreenWidth(), stage.getViewport().getScreenHeight());
        imageLists.get("menus").add(menu);
        mapButton = new Image(AssetManagerUtil.getMapButton());
        mapButton.setPosition(680, 480);
        changeMapMenu = false;
        this.decoScale[0] = 2;
        this.decoScale[1] = 2;
        this.decoScale[2] = 2;
        addEvenMoreListeners();
        makeLabels();
        makeOptionListeners();
        addMaps();
        addMoreListeners();
        stage.addActor(continueButton);
        stage.addActor(gameSpeed);
        stage.addActor(laserSpeed);
        stage.addActor(funMode);
        stage.addActor(volumeSlider);
        stage.addActor(playSong);
        makeClickableButtons(stage);
    }

    private void addEvenMoreListeners() {
        Pixmap colur = new Pixmap(100, 10, Pixmap.Format.RGBA8888);
        Pixmap colur2 = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        Pixmap colur3 = new Pixmap(2, 2, Pixmap.Format.RGB888);
        Pixmap colur4 = new Pixmap(10, 1, Pixmap.Format.RGBA8888);
        colur3.setColor(Color.CHARTREUSE);
        colur3.fill();
        colur4.setColor(Color.ORANGE.set(0, 0, 0, 0));
        colur4.fill();
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle1 = new Slider.SliderStyle();
        colur.setColor(Color.BLACK);
        colur.fill();
        colur2.setColor(Color.WHITE);
        colur2.fill();
        sliderStyle.background = new Image(new Texture(colur)).getDrawable();
        sliderStyle.knob = new Image(new Texture(colur2)).getDrawable();
        sliderStyle1.background = new Image(new Texture(colur4)).getDrawable();
        sliderStyle1.knob = new Image(new Texture(colur3)).getDrawable();
        Slider decor1 = new Slider(0, 10, 0.01f, false, sliderStyle1);
        decor1.setSize(170, 0.2f);
        decor1.setPosition(420, 350);
        sliders.add(decor1);
        decoWidth[0] = 140;
        Image birdImage = new Image(new Texture("assets/icons/Icon.png"));
        birdImage.setSize(4, 4);
        sliderStyle1.knob = birdImage.getDrawable();
        Slider decor2 = new Slider(0, 10, 0.01f, false, sliderStyle1);
        decor2.setSize(225, 0.2f);
        decor2.setPosition(405, 485);
        sliders.add(decor2);
        Slider decor3 = new Slider(0, 10, 0.01f, false, sliderStyle1);
        decor3.setSize(180, 0.2f);
        decor3.setPosition(430, 420);
        sliders.add(decor3);
        decoWidth[1] = decor2.getWidth();
        decoWidth[2] = decor3.getWidth();
        continueButton = new Image(new Texture(Gdx.files.internal("menu/continue.png")));
        continueButton.setPosition(350, 490);
        continueButton.setWidth(continueButton.getWidth() + 150);
        continueButton.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                continueButton.setColor(Color.GOLD);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                continueButton.setColor(Color.YELLOW);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                Continue = true;
            }
        });
    }

    private void makeOptionListeners() {
        gameSpeed.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                gameSpeed.setColor(Color.RED);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                gameSpeed.setColor(Color.WHITE);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gSpeed == 0) {
                    gameSpeed.setText("Game Speed: fast");
                    gSpeed++;
                    getEvents().setGameSpeed("fast");
                } else if (gSpeed == 1) {
                    gameSpeed.setText("Game Speed: fastest");
                    gSpeed++;
                    getEvents().setGameSpeed("fastest");
                } else if (gSpeed == 2) {
                    gameSpeed.setText("Game Speed: normal");
                    getEvents().setGameSpeed("normal");
                    gSpeed = 0;
                }
            }
        });
        laserSpeed.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                laserSpeed.setColor(Color.RED);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                laserSpeed.setColor(Color.WHITE);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (lSpeed == 1) {
                    laserSpeed.setText("Laser Speed: normal");
                    lSpeed++;
                    getEvents().setLaserSpeed("normal");
                } else if (lSpeed == 2) {
                    laserSpeed.setText("Laser Speed: fast");
                    lSpeed = 0;
                    getEvents().setLaserSpeed("fast");
                } else if (lSpeed == 0) {
                    laserSpeed.setText("Laser Speed: slow");
                    getEvents().setLaserSpeed("slow");
                    lSpeed++;
                }
            }
        });
        funMode.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                funMode.setColor(Color.RED);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                funMode.setColor(Color.WHITE);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (fun == 0) {
                    funMode.setText("FunMode: On");
                    isFunMode = true;
                    fun = 1;
                } else {
                    funMode.setText("FunMode: Off");
                    isFunMode = false;
                    fun = 0;
                }
            }
        });
    }

    private void addMoreListeners() {
        left.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (mapId == 0)
                    mapId = 1;
                else
                    mapId -= 1;
                return true;
            }
        });
        right.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (mapId == 1)
                    mapId = 0;
                else
                    mapId += 1;
                return true;
            }
        });

        playSong.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                playSong.setColor(Color.GREEN);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                funMode.setColor(Color.WHITE);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                String text = playSong.getText().toString();
                if ("Play a song: ".equals(text)) {
                    Song = AssetManagerUtil.manager.get(AssetManagerUtil.SOUNDTRACK);
                    Song.setVolume(0.1f * AssetManagerUtil.volume);
                    Song.play();
                    Song.setLooping(true);
                    playSong.setText("Click to stop playing.");
                } else {
                    Song.stop();
                    playSong.setText("Play a song: ");
                }
            }
        });
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        Image birdImage = new Image(new Texture("assets/icons/Icon.png"));
        birdImage.setSize(15, 20);
        birdImage.scaleBy(100);
        Pixmap pixmap = new Pixmap(10, 8, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        sliderStyle.background = new Image(new Texture(pixmap)).getDrawable();
        sliderStyle.knob = birdImage.getDrawable();
        volumeSlider = new Slider(0f, 1f, 0.1f, false, sliderStyle);
        volumeSlider.setValue(0.6f);
        volumeSlider.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                AssetManagerUtil.volume = volumeSlider.getValue();
                volume.setText("Volume " + Math.round(volumeSlider.getValue() * 100.0) / 100.0);
            }
        });

        volumeSlider.setPosition(750, 600);
    }

    public boolean isResume(IGame game) {
        if (Continue) {
            Continue = false;
            resume = false;
            return true;
        }
        if (resume && !events.hasWaitEvent()) {
            if (isFunMode && !events.hasWaitEvent())
                game.funMode();
            else {
                game.startUp();
            }
            startGame = 1;
            resume = false;
            return true;
        }
        return false;
    }

    public boolean isChangeMap() {
        if (changeMap) {
            changeMap = false;
            return true;
        }
        return false;
    }

    public int getMapId() {
        return this.mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public Events getEvents() {
        return this.events;
    }

    public Image getMap() {
        return imageLists.get("maps").get(mapId);
    }

    public void reloadStage(Stage stage) {
        for (Image image : imageLists.get("buttons"))
            stage.addActor(image);
        stage.addActor(gameSpeed);
        stage.addActor(laserSpeed);
        stage.addActor(funMode);
        stage.addActor(continueButton);
        stage.addActor(volume);
        stage.addActor(volumeSlider);
        stage.addActor(playSong);
    }

    public void drawMenu(SpriteBatch batch, Stage stage) {
        imageLists.get("menus").get(0).draw(batch, 1);
        if (!changeMapMenu) {
            for (Image image : imageLists.get("buttons"))
                image.draw(batch, 1);
            gameSpeed.draw(batch, 1);
            laserSpeed.draw(batch, 1);
            funMode.draw(batch, 1);
            volumeSlider.draw(batch, 1);
            volume.draw(batch, 1);
            playSong.draw(batch, 1);
            drawSliders(batch);
            if (startGame == 1)
                continueButton.draw(batch, 1);
        } else {
            imageLists.get("maps").get(mapId).draw(batch, 1);
            left.draw(batch, 1);
            right.draw(batch, 1);
            mapButton.draw(batch, 1);
        }
        if (changeMap)
            changeMapMenu = false;
        stage.act();
    }

    private void drawSliders(SpriteBatch batch) {
        for (int i = 0; i < sliders.size(); i++) {
            sliders.get(i).draw(batch, 1);
            superit2 -= Gdx.graphics.getDeltaTime() * 10;
            if (superit < 1) {
                superit += Gdx.graphics.getDeltaTime() * 10;
                sliders.get(i).setValue(decoIterate[i] += Gdx.graphics.getDeltaTime() * decoScale[i] * 2);
            } else if (superit2 < -10) {
                superit = superit2;
                superit2 = 0;
            }
            if (superit >= 1) {
                sliders.get(i).setValue(decoIterate[i] += Gdx.graphics.getDeltaTime() * decoScale[i] / 2);
                superit -= Gdx.graphics.getDeltaTime() * 10;
            }
            checkForFlips(i);
        }
    }

    private void checkForFlips(int i) {
        if (decoH1 && i == 0 || decoH2 && i == 1 || decoH3 && i == 2) {
            if (!flip1 && i == 0 || !flip2 && i == 1 || !flip3 && i == 2) {
                if (decoIterate[i] < sliders.get(i).getMinValue() + dt) {
                    flipH(i);
                }
            }
            if (flip1 && decoIterate[i] > sliders.get(i).getMaxValue() - dt && i == 0 || flip2 && decoIterate[i] > sliders.get(i).getMaxValue() - dt && i == 1
                    || flip3 && decoIterate[i] > sliders.get(i).getMaxValue() - dt && i == 2) {
                flipH2(i);
            }
        } else {
            if (!flip1 && i == 0 || !flip2 && i == 1 || !flip3 && i == 2) {
                if (decoIterate[i] > sliders.get(i).getMaxValue() - dt) {
                    flipV(i);
                }
            }
            if (flip1 && decoIterate[i] < sliders.get(i).getMinValue() + dt && i == 0 || flip2 && decoIterate[i] < sliders.get(i).getMinValue() + dt && i == 1 || flip3 && decoIterate[i] < sliders.get(i).getMinValue() + dt && i == 2) {
                flipV2(i);
            }
        }
    }

    private void makeLabels() {
        gameSpeed = new Label("Game Speed: normal", labelStyle);
        laserSpeed = new Label("Laser Speed: normal", labelStyle);
        funMode = new Label("FunMode: Off", labelStyle);
        playSong = new Label("Play a song: ", labelStyle);
        volume = new Label("Volume ", labelStyle);
        volume.setPosition(750, 620);
        gSpeed = 0;
        lSpeed = 2;
        funMode.setPosition(750, 560);
        laserSpeed.setPosition(750, 535);
        gameSpeed.setPosition(750, 510);
        playSong.setPosition(750, 640);
        left = new Label(" < ", labelStyle);
        right = new Label(" > ", labelStyle);
        left.setSize(120, 120);
        right.setSize(120, 120);
        left.scaleBy(2);
        right.scaleBy(2);
        left.setPosition(40, 300);
        right.setPosition(665, 300);
    }

    private void makeClickableButtons(Stage stage) {
        ArrayList<Image> clickableButtons = new ArrayList<>();
        TextureRegion[][] buttons = TextureRegion.split(AssetManagerUtil.getButtons(), 200, 50);
        int y = 525;
        for (int i = 0; i < 3; i++) {
            clickableButtons.add(new Image(buttons[i][0]));
            clickableButtons.get(i).setY(y -= 75);
            clickableButtons.get(i).setX(420);
            stage.addActor(clickableButtons.get(i));
        }
        makeClickListeners(stage, clickableButtons);
    }

    private void makeClickListeners(Stage stage, ArrayList<Image> clickableButtons) {
        mapButton.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                mapButton.setColor(Color.RED);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                mapButton.setColor(Color.BLUE);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeMap = true;
                changeMapMenu = false;
                startGame = 0;
                stage.clear();
                stage.addActor(gameSpeed);
                stage.addActor(laserSpeed);
                stage.addActor(funMode);
                stage.addActor(continueButton);
                stage.addActor(volume);
                stage.addActor(volumeSlider);
                stage.addActor(playSong);
                for (Image image : imageLists.get("buttons"))
                    stage.addActor(image);
            }
        });
        clickableButtons.get(0).addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                clickableButtons.get(0).setColor(Color.RED);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                clickableButtons.get(0).setColor(Color.BLUE);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                resume = true;
            }
        });
        clickableButtons.get(1).addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                clickableButtons.get(1).setColor(Color.RED);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                clickableButtons.get(1).setColor(Color.BLUE);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeMapMenu = true;
                stage.clear();
                stage.addActor(left);
                stage.addActor(right);
                stage.addActor(mapButton);
            }
        });
        clickableButtons.get(2).addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                clickableButtons.get(2).setColor(Color.RED);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                clickableButtons.get(2).setColor(Color.BLUE);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        imageLists.put("buttons", clickableButtons);
    }

    public void addMaps() {
        ArrayList<Image> maps = new ArrayList<>();
        maps.add(new Image(new Texture(Gdx.files.internal("maps/models/map0.png"))));
        maps.add(new Image(new Texture(Gdx.files.internal("maps/models/map1.png"))));
        for (Image image : maps)
            image.setPosition(75, 115);
        imageLists.put("maps", maps);
    }

    public void flipV(int i) {
        Slider slider = new Slider(0, 5, 0.1f, true, sliderStyle1);
        slider.setSize(10, 30);
        slider.setPosition(sliders.get(i).getX() + sliders.get(i).getWidth() - slider.getWidth(), sliders.get(i).getY() - slider.getHeight());
        slider.setValue(5 - dt);
        decoIterate[i] = 5 - dt;
        decoScale[i] = -decoScale[i];
        sliders.set(i, slider);
        if (i == 0)
            decoH1 = true;
        if (i == 1)
            decoH2 = true;
        if (i == 2)
            decoH3 = true;
    }

    public void flipH(int i) {
        Slider slider = new Slider(0, 10, 0.01f, false, sliderStyle1);
        slider.setSize(decoWidth[i], 0.2f);
        slider.setPosition(sliders.get(i).getX() - slider.getWidth() + sliders.get(i).getWidth(), sliders.get(i).getY());
        slider.setValue(10 - dt);
        decoIterate[i] = 10 - dt;
        sliders.set(i, slider);
        if (i == 0) {
            decoH1 = false;
            flip1 = true;
        }
        if (i == 1) {
            decoH2 = false;
            flip2 = true;
        }
        if (i == 2) {
            decoH3 = false;
            flip3 = true;
        }
    }

    private void flipV2(int i) {
        Slider slider = new Slider(0, 5, 0.1f, true, sliderStyle1);
        slider.setSize(10, 30);
        slider.setPosition(sliders.get(i).getX(), sliders.get(i).getY());
        slider.setValue(0 + dt);
        decoIterate[i] = 0 + dt;
        decoScale[i] = Math.abs(decoScale[i]);
        sliders.set(i, slider);
        if (i == 0)
            decoH1 = true;
        if (i == 1)
            decoH2 = true;
        if (i == 2)
            decoH3 = true;
    }

    private void flipH2(int i) {
        Slider slider = new Slider(0, 10, 0.01f, false, sliderStyle1);
        slider.setSize(decoWidth[i], 0.2f);
        slider.setPosition(sliders.get(i).getX(), sliders.get(i).getY() + sliders.get(i).getHeight());
        slider.setValue(0 + dt);
        sliders.set(i, slider);
        decoIterate[i] = 0 + dt;
        decoScale[i] = Math.abs(decoScale[i]);
        if (i == 0) {
            decoH1 = false;
            flip1 = false;
        }
        if (i == 1) {
            decoH2 = false;
            flip2 = false;
        }
        if (i == 2) {
            decoH3 = false;
            flip3 = false;
        }

    }

}
