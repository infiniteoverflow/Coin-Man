package com.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	Texture coin;
	Texture bomb;
	Rectangle manRectangle;

	ArrayList<Integer> coinXs = new ArrayList<Integer>();
	ArrayList<Integer> coinYs = new ArrayList<Integer>();
	ArrayList<Rectangle> coinRectangles = new ArrayList<Rectangle>();

	ArrayList<Integer> bombXs = new ArrayList<Integer>();
	ArrayList<Integer> bombYs = new ArrayList<Integer>();
	ArrayList<Rectangle> bombRectangles = new ArrayList<Rectangle>();


	Random random;

	int manState = 0;
	int pause = 0;
	int coinCount;
	int bombCount;

	float gravity = 0.2f;
	float velocity = 0;
	int manY;

	@Override
	public void create () {
		batch = new SpriteBatch();

		background = new Texture("bg.png");

		man = new Texture[4];
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");

		manY = Gdx.graphics.getWidth()/2-background.getWidth();

		coin = new Texture("coin.png");
		bomb = new Texture("bomb.png");
		random = new Random();

	}

	public void makeCoin()
	{
		float height = random.nextFloat()*Gdx.graphics.getHeight();

		coinYs.add((int)height);
		coinXs.add(Gdx.graphics.getWidth());
	}

	public void bombCoin()
	{
		float height = random.nextFloat()*Gdx.graphics.getHeight();

		bombYs.add((int)height);
		bombXs.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		// Coins
        if (coinCount < 100) {
            coinCount++;
        } else {
            coinCount = 0;
			makeCoin();
        }

        coinRectangles.clear();

        for (int i=0;i < coinXs.size();i++) {
            batch.draw(coin, coinXs.get(i), coinYs.get(i));
            coinXs.set(i, coinXs.get(i) - 4);
			coinRectangles.add(new Rectangle(coinXs.get(i), coinYs.get(i), coin.getWidth(), coin.getHeight()));
        }

        // Bombs
		if (bombCount < 250) {
			bombCount++;
		} else {
			bombCount = 0;
			bombCoin();
		}

		bombRectangles.clear();

		for (int i=0;i < bombXs.size();i++) {
			batch.draw(bomb, bombXs.get(i), bombYs.get(i));
			bombXs.set(i, bombXs.get(i) - 4);
			bombRectangles.add(new Rectangle(bombXs.get(i), bombYs.get(i), bomb.getWidth(), bomb.getHeight()));
		}

        // To make the man jump
		if(Gdx.input.justTouched()) {
			velocity -= 10;
		}

		//Time delay between the feets to display translation effect
		if(pause < 8) {
			pause++;
		} else {
			pause = 0;
			if(manState<3) {
				manState++;
			} else {
				manState = 0;
			}
		}

		velocity += gravity;
		manY -= velocity;

		if(manY<=0) {
			manY = 0;
			velocity = 0;
		}


		batch.draw(man[manState],Gdx.graphics.getWidth()/2-background.getWidth(),manY);

		manRectangle = new Rectangle(Gdx.graphics.getWidth()/2-background.getWidth(),manY,man[manState].getWidth(),man[manState].getHeight());
		for(int i=0;i<coinRectangles.size();i++)
		{
			if(Intersector.overlaps(manRectangle,coinRectangles.get(i))); {
				Gdx.app.log("Coin !!","Collision");
		}
		}

		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
