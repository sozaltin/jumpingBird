package com.example.semi.survivorbird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import java.util.Random;

public class SurvivorBird extends ApplicationAdapter {
	SpriteBatch batch;          //sprite denen objelerle çalışıyoruz   sprite =kuş,arkaplan,objeler
	com.badlogic.gdx.graphics.Texture background;           // bir objenın ımagesi
	Texture bird;
	Texture bee1;
	Texture bee2;
	Texture bee3;
	float birdX=0;
	float birdY=0;
	int gameState=0;  // oyun baslamasını kontrol eder
	float velocity =0;
	float gravity =1.1f; //kuşun yere düşme hızı
	float enemyVelocity =6; // rakibin hızı
	Random random ;
	Circle birdCircle;  // kuşun çarpışmayı gerçekleştirmesi için etrafına circle çiziyoruz

	int score =0;  // skorlamak
	int scoredEnemy=0;
	BitmapFont font;
	BitmapFont fontGameOver;

	ShapeRenderer shapeRenderer;

	int numberOfenemies = 5;
	float [] enemyX = new float[numberOfenemies];
	float [] enemyOffset = new float[numberOfenemies];
	float [] enemyOffset2 = new float[numberOfenemies];
	float [] enemyOffset3 = new float[numberOfenemies];
	float distance =0;

	Circle [] enemyCircles;
	Circle [] enemyCircles2;
	Circle [] enemyCircles3;


	@Override
	public void create () {  //oncreate aynısı oyun basladıgında ne olucaksa burada olucak
		batch= new SpriteBatch();
		background = new com.badlogic.gdx.graphics.Texture("background.png");
		bird = new Texture("bird.png");
		bee1 = new Texture("bee.png");
		bee2 = new Texture("bee.png");
		bee3 = new Texture("bee.png");

		distance = Gdx.graphics.getWidth()/3;  // kuşların arasındakı mesafe

		random = new Random();  // arıların farklı eksenlerde oluşması için

		 birdX=Gdx.graphics.getWidth()/5;
		 birdY=Gdx.graphics.getHeight()/2;
		 birdCircle = new Circle( );

		  shapeRenderer = new ShapeRenderer();

		  font = new BitmapFont();
		  font.setColor(com.badlogic.gdx.graphics.Color.WHITE);
		  font.getData().setScale(4);    // fontun boyutu
		fontGameOver = new BitmapFont();
		fontGameOver.setColor(Color.RED);
		fontGameOver.getData().setScale(6);

		 enemyCircles=new Circle[numberOfenemies];
		enemyCircles2=new Circle[numberOfenemies];
		enemyCircles3=new Circle[numberOfenemies];

		 for(int i=0; i<numberOfenemies; i++){

			 enemyOffset [i] = (random.nextFloat() -0.5f) *(Gdx.graphics.getHeight() -200);
			 enemyOffset2 [i] = (random.nextFloat()-0.5f) *(Gdx.graphics.getHeight()-200);
			 enemyOffset3 [i] = (random.nextFloat()-0.5f) *(Gdx.graphics.getHeight()-200);


		 	enemyX[i] = Gdx.graphics.getWidth()-bee1.getWidth()/2+i*distance;

		 	enemyCircles[i] = new Circle();
			 enemyCircles2[i] = new Circle();
			 enemyCircles3[i] = new Circle();
		 }

	}

	// oyun devam ettıgı surece devamlı cagırılan bır method
	// sureklı olmasını ıstedıgımız seylerı buraya yazıyoruz
	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if(gameState==1 ) {

			if (enemyX[scoredEnemy] < Gdx.graphics.getWidth() / 5) {
				score++;
				if (scoredEnemy < numberOfenemies - 1) {
					scoredEnemy++;
				} else {
					scoredEnemy = 0;
				}
			}

			if (Gdx.input.isTouched()) {
				velocity = (float) (-Gdx.graphics.getHeight() * 0.015);   // alternatifi =>   velocity = -15  kuşun zıplaması
			}


			for (int i = 0; i < numberOfenemies; i++) {
				if (enemyX[i] < 0) {
					enemyX[i] = enemyX[i] + numberOfenemies * distance;

					enemyOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffset2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffset3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
				} else {
					enemyX[i] = enemyX[i] - enemyVelocity;
				}

				batch.draw(bee1, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffset[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 10);
				batch.draw(bee2, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffset2[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 10);
				batch.draw(bee3, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffset3[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 10);


				enemyCircles[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight() / 2 + enemyOffset[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 30);
				enemyCircles2[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight() / 2 + enemyOffset2[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 30);
				enemyCircles3[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight() / 2 + enemyOffset3[i] + Gdx.graphics.getHeight() / 20, Gdx.graphics.getWidth() / 30);
			}

			if (birdY > 0  ) {    // y ekseninde aşağıya doğru gitmesini engellıyor

					velocity = velocity + gravity;
					birdY = birdY - velocity;
				if( birdY >=Gdx.graphics.getHeight()){
					birdY=Gdx.graphics.getHeight() -80;
				}
			}

			else {
				gameState = 2;
			}


		}
		else if(gameState==0){
			if(Gdx.input.isTouched()){   // tıkladıgında oyuna baslasın
				gameState=1;
			}
		}
		else if(gameState==2){

			fontGameOver.draw(batch,"GAME OVER",Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);

			if(Gdx.input.isTouched()){   // tıkladıgında oyuna baslasın
				gameState=1;
				birdY=Gdx.graphics.getHeight()/2;   // oyun tekrar basladıgında kuşu başlangıç pozisyonuna alır

				for(int i=0; i<numberOfenemies; i++){

					enemyOffset [i] = (random.nextFloat() -0.5f) *(Gdx.graphics.getHeight() -200);
					enemyOffset2 [i] = (random.nextFloat()-0.5f) *(Gdx.graphics.getHeight()-200);
					enemyOffset3 [i] = (random.nextFloat()-0.5f) *(Gdx.graphics.getHeight()-200);


					enemyX[i] = Gdx.graphics.getWidth()-bee1.getWidth()/2+i*distance;

					enemyCircles[i] = new Circle();
					enemyCircles2[i] = new Circle();
					enemyCircles3[i] = new Circle();
				}
				velocity =0;
				scoredEnemy=0;
				score=0;

			}
		}


		batch.draw(bird,birdX,birdY,Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10); // kuşun boyutları ve yeri

		font.draw(batch,String.valueOf(score),100,200); // fontun y ekranda gözükeceği yer

		batch.end();

		birdCircle.set(birdX + Gdx.graphics.getWidth()/30,birdY+Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);

		/*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);
		*/

		for(int i=0 ; i<numberOfenemies ; i++){

			/*shapeRenderer.circle(enemyX[i]+Gdx.graphics.getWidth()/30,Gdx.graphics.getHeight()/2 + enemyOffset[i] +Gdx.graphics.getHeight()/20 ,Gdx.graphics.getWidth()/30);
			shapeRenderer.circle(enemyX[i]+Gdx.graphics.getWidth()/30,Gdx.graphics.getHeight()/2 + enemyOffset2[i] +Gdx.graphics.getHeight()/20 ,Gdx.graphics.getWidth()/30);
			shapeRenderer.circle(enemyX[i]+Gdx.graphics.getWidth()/30,Gdx.graphics.getHeight()/2 + enemyOffset3[i] +Gdx.graphics.getHeight()/20 ,Gdx.graphics.getWidth()/30);
			*/

			// çarpışmalar Intersector ile kontrol ediliyor
			if(Intersector.overlaps(birdCircle,enemyCircles[i]) || Intersector.overlaps(birdCircle,enemyCircles2[i]) || Intersector.overlaps(birdCircle,enemyCircles3[i]) ){
					gameState =2;
			}
		}
		  //shapeRenderer.end();
	}
	@Override
	public void dispose () {

	}
}
