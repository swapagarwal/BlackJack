package com.swap.blackjack;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity implements OnClickListener,
                                                      OnSeekBarChangeListener, ViewFactory {

  /**
   * Called when the activity is first created.
   */

  public static LinearLayout layout;

  // All elements used are defined here
  public static TextView tvMoney, tvHighestScore, tvDealerScore, tvYourScore, tvBet;
  private ImageSwitcher ivDealerCard1, ivDealerCard2, ivDealerCard3, ivDealerCard4,
      ivDealerCard5;
  private ImageSwitcher ivYourCard1, ivYourCard2, ivYourCard3, ivYourCard4, ivYourCard5;
  private ImageSwitcher ivSplitCard1, ivSplitCard2, ivSplitCard3, ivSplitCard4, ivSplitCard5;
  private Button btnPlaceBet;
  private Button btnHit, btnStand, btnSurrender, btnChallenge;
  private SeekBar sbBetAmount;
  static final String[] challengeString = new String[3];

  // SoundPool soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

  MediaPlayer mp;

  // All local variables here
  static int _money = 0;
  int _bet = 0;
  int _dealerScore = 0, _playerScore = 0;
  int _dealerCardNumber = 0, _playerCardNumber = 0;
  int _randomNumber;
  int _splitCard;
  int _splitDealerCard;
  int _splitScore;
  int _splitBet = 0;
  int _highestScore;
  boolean splitting;

  // To make sure no card comes twice
  ArrayList<Integer> _alCardsTracking = new ArrayList<Integer>();

  // Dealer and Player Aces Check
  char[] _dealerCardArray = new char[]{'0', '0', '0', '0', '0'};
  char[] _playerCardArray = new char[]{'0', '0', '0', '0', '0'};

  // Dealer and Player Score Count
  int[] _dealerScoreCount = new int[]{0, 0, 0, 0, 0};
  int[] _playerScoreCount = new int[]{0, 0, 0, 0, 0};

  // Internal Storage
  private final String saveFileName = "savingHighScoreOfBlackJack";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Setting up all variables here
    setupVariables();
    // Initializing soundPool

    // soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

    // ImageSwitcher
    imageSwitcherStuff();

    // Starting stuff
    _money = 500;
    tvMoney.setText(" $ " + _money);
    sbBetAmount.setMax(_money);
    hidePlayButtons();
    loadingHighScore();

//		// Remove title bar
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//		// Remove notification bar
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
  }

  private void imageSwitcherStuff() {

    ivDealerCard1.setFactory(this);
    ivDealerCard2.setFactory(this);
    ivDealerCard3.setFactory(this);
    ivDealerCard4.setFactory(this);
    ivDealerCard5.setFactory(this);

    ivYourCard1.setFactory(this);
    ivYourCard2.setFactory(this);
    ivYourCard3.setFactory(this);
    ivYourCard4.setFactory(this);
    ivYourCard5.setFactory(this);

    ivSplitCard1.setFactory(this);
    ivSplitCard2.setFactory(this);
    ivSplitCard3.setFactory(this);
    ivSplitCard4.setFactory(this);
    ivSplitCard5.setFactory(this);

    ivDealerCard1.setInAnimation(AnimationUtils.loadAnimation(this,
                                                              android.R.anim.slide_in_left));
    ivDealerCard1.setOutAnimation(AnimationUtils.loadAnimation(this,
                                                               android.R.anim.slide_out_right));

    ivDealerCard2.setInAnimation(AnimationUtils.loadAnimation(this,
                                                              android.R.anim.slide_in_left));
    ivDealerCard2.setOutAnimation(AnimationUtils.loadAnimation(this,
                                                               android.R.anim.slide_out_right));

    ivDealerCard3.setInAnimation(AnimationUtils.loadAnimation(this,
                                                              android.R.anim.slide_in_left));
    ivDealerCard3.setOutAnimation(AnimationUtils.loadAnimation(this,
                                                               android.R.anim.slide_out_right));

    ivDealerCard4.setInAnimation(AnimationUtils.loadAnimation(this,
                                                              android.R.anim.slide_in_left));
    ivDealerCard4.setOutAnimation(AnimationUtils.loadAnimation(this,
                                                               android.R.anim.slide_out_right));

    ivDealerCard5.setInAnimation(AnimationUtils.loadAnimation(this,
                                                              android.R.anim.slide_in_left));
    ivDealerCard5.setOutAnimation(AnimationUtils.loadAnimation(this,
                                                               android.R.anim.slide_out_right));

    ivYourCard1.setInAnimation(AnimationUtils.loadAnimation(this,
                                                            android.R.anim.slide_in_left));
    ivYourCard1.setOutAnimation(AnimationUtils.loadAnimation(this,
                                                             android.R.anim.slide_out_right));

    ivYourCard2.setInAnimation(AnimationUtils.loadAnimation(this,
                                                            android.R.anim.slide_in_left));
    ivYourCard2.setOutAnimation(AnimationUtils.loadAnimation(this,
                                                             android.R.anim.slide_out_right));

    ivYourCard3.setInAnimation(AnimationUtils.loadAnimation(this,
                                                            android.R.anim.slide_in_left));
    ivYourCard3.setOutAnimation(AnimationUtils.loadAnimation(this,
                                                             android.R.anim.slide_out_right));

    ivYourCard4.setInAnimation(AnimationUtils.loadAnimation(this,
                                                            android.R.anim.slide_in_left));
    ivYourCard4.setOutAnimation(AnimationUtils.loadAnimation(this,
                                                             android.R.anim.slide_out_right));

    ivYourCard5.setInAnimation(AnimationUtils.loadAnimation(this,
                                                            android.R.anim.slide_in_left));
    ivYourCard5.setOutAnimation(AnimationUtils.loadAnimation(this,
                                                             android.R.anim.slide_out_right));

    ivDealerCard1.setImageResource(R.drawable.default_red);
    ivDealerCard2.setImageResource(R.drawable.default_red);
    ivDealerCard3.setImageResource(R.drawable.default_red);
    ivDealerCard4.setImageResource(R.drawable.default_red);
    ivDealerCard5.setImageResource(R.drawable.default_red);

    ivYourCard1.setImageResource(R.drawable.default_blue);
    ivYourCard2.setImageResource(R.drawable.default_blue);
    ivYourCard3.setImageResource(R.drawable.default_blue);
    ivYourCard4.setImageResource(R.drawable.default_blue);
    ivYourCard5.setImageResource(R.drawable.default_blue);

  }

  private void resetEveryThing() {

    ivDealerCard1.setImageResource(R.drawable.default_red);
    ivDealerCard2.setImageResource(R.drawable.default_red);
    ivDealerCard3.setImageResource(R.drawable.default_red);
    ivDealerCard4.setImageResource(R.drawable.default_red);
    ivDealerCard5.setImageResource(R.drawable.default_red);

    ivYourCard1.setImageResource(R.drawable.default_blue);
    ivYourCard2.setImageResource(R.drawable.default_blue);
    ivYourCard3.setImageResource(R.drawable.default_blue);
    ivYourCard4.setImageResource(R.drawable.default_blue);
    ivYourCard5.setImageResource(R.drawable.default_blue);

    ivSplitCard1.setVisibility(View.GONE);
    ivSplitCard2.setVisibility(View.GONE);
    ivSplitCard3.setVisibility(View.GONE);
    ivSplitCard4.setVisibility(View.GONE);
    ivSplitCard5.setVisibility(View.GONE);

    btnPlaceBet.setVisibility(View.VISIBLE);
    hidePlayButtons();
    _dealerCardNumber = _playerCardNumber = 0;
    _dealerScore = _playerScore = 0;
    _alCardsTracking.clear();

    for (int i = 0; i < 5; i++) {
      _dealerCardArray[i] = '0';
      _dealerScoreCount[i] = 0;
      _playerCardArray[i] = '0';
      _playerScoreCount[i] = 0;
    }

    sbBetAmount.setProgress(0);
    sbBetAmount.setMax(_money);

    showTextViews();

    // Setting up high score
    highScoreCompare();

    // User don't have any money left
    if (_money <= 0) {
      playAllOverAgainAlertBox();
    }

  }

  // Setting up high score
  private void highScoreCompare() {
    if (_money > _highestScore) {
      _highestScore = _money;
      tvHighestScore.setText("Highest : " + _highestScore);
    }
  }

  // Saving highScore
  private void savingHighScore() {

    try {
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
          openFileOutput(saveFileName, MODE_PRIVATE)));
      writer.write("" + _highestScore);
      writer.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  // Loading high score
  private void loadingHighScore() {

    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(
          openFileInput(saveFileName)));
      String highScore = reader.readLine();
      _highestScore = Integer.parseInt(highScore);
      reader.close();
      tvHighestScore.setText("Highest : " + _highestScore);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // setupVariables
  private void setupVariables() {

    layout = (LinearLayout) findViewById(R.id.parentLayout);

    tvMoney = (TextView) findViewById(R.id.tvMoney);
    tvHighestScore = (TextView) findViewById(R.id.tvHighest);
    tvDealerScore = (TextView) findViewById(R.id.tvDealer);
    tvYourScore = (TextView) findViewById(R.id.tvYou);
    tvBet = (TextView) findViewById(R.id.tvBet);

    ivDealerCard1 = (ImageSwitcher) findViewById(R.id.ivDealerCard1);
    ivDealerCard2 = (ImageSwitcher) findViewById(R.id.ivDealerCard2);
    ivDealerCard3 = (ImageSwitcher) findViewById(R.id.ivDealerCard3);
    ivDealerCard4 = (ImageSwitcher) findViewById(R.id.ivDealerCard4);
    ivDealerCard5 = (ImageSwitcher) findViewById(R.id.ivDealerCard5);

    ivYourCard1 = (ImageSwitcher) findViewById(R.id.ivYourCard1);
    ivYourCard2 = (ImageSwitcher) findViewById(R.id.ivYourCard2);
    ivYourCard3 = (ImageSwitcher) findViewById(R.id.ivYourCard3);
    ivYourCard4 = (ImageSwitcher) findViewById(R.id.ivYourCard4);
    ivYourCard5 = (ImageSwitcher) findViewById(R.id.ivYourCard5);

    ivSplitCard1 = (ImageSwitcher) findViewById(R.id.ivSplitCard1);
    ivSplitCard2 = (ImageSwitcher) findViewById(R.id.ivSplitCard2);
    ivSplitCard3 = (ImageSwitcher) findViewById(R.id.ivSplitCard3);
    ivSplitCard4 = (ImageSwitcher) findViewById(R.id.ivSplitCard4);
    ivSplitCard5 = (ImageSwitcher) findViewById(R.id.ivSplitCard5);

    btnHit = (Button) findViewById(R.id.btnHit);
    btnStand = (Button) findViewById(R.id.btnStand);
    btnSurrender = (Button) findViewById(R.id.btnSurrender);

    btnHit.setOnClickListener(this);
    btnStand.setOnClickListener(this);
    btnSurrender.setOnClickListener(this);

    btnPlaceBet = (Button) findViewById(R.id.btnPlaceBet);
    btnPlaceBet.setOnClickListener(this);
    Button btnExit = (Button) findViewById(R.id.btnExit);
    btnExit.setOnClickListener(this);
    Button btnHelp = (Button) findViewById(R.id.btnHelp);
    btnHelp.setOnClickListener(this);
    btnChallenge = (Button)findViewById(R.id.btnChallenge);
    btnChallenge.setOnClickListener(this);
    

    sbBetAmount = (SeekBar) findViewById(R.id.sbBetAmount);
    sbBetAmount.setOnSeekBarChangeListener(this);
    
    challengeString[0] = getString(R.string.challengePt1);
    challengeString[1] = getString(R.string.challengePt2);
    challengeString[2] = getString(R.string.challengePt3);

  }

  public void onClick(View v) {

    // Making some sound here
    {
      SharedPreferences prefs = PreferenceManager
          .getDefaultSharedPreferences(getApplicationContext());
      boolean gameSound = prefs.getBoolean("pref_cb_sound", true);
      if (gameSound) {
        mp = MediaPlayer
            .create(MainActivity.this, R.raw.btn_click);
        mp.start();
      }

    }
    if (_playerCardNumber > 4 || _dealerCardNumber > 4) {
      return;
    }

    switch (v.getId()) {

      case R.id.btnPlaceBet:

        if (_bet > 0) {
          _money -= _bet;

          showPlayButtons();
          btnPlaceBet.setVisibility(View.INVISIBLE);
          gameStart();
        } else {
          Toast.makeText(MainActivity.this, "Bet Some Money First",
                         Toast.LENGTH_SHORT).show();
        }

        break;

      // Hit
      case R.id.btnHit:

        btnHitClick();
        break;

      // Stand
      case R.id.btnStand:

        btnStandClick();
        alertBox();
        break;

      case R.id.btnSurrender:

        // Surrender
        _money += _bet / 2;
        Toast.makeText(MainActivity.this,
                       "You Surrendered, You got half of your money back.",
                       Toast.LENGTH_SHORT).show();
        resetEveryThing();
        break;

      case R.id.btnExit:
        savingHighScore();
        finish();
        break;

      case R.id.btnHelp:
        // Help here

        Intent intent = new Intent(MainActivity.this, Help.class);
        startActivity(intent);
        break;
       
      case R.id.btnChallenge:
    	  Intent sendIntent = new Intent(Intent.ACTION_SEND);
    	  sendIntent.setType("text/plain");
    	  String message = challengeString[0] + " "+ _playerScore + " " + challengeString[1]
    			  + " "+_highestScore+ ". " + challengeString[2]; 
    	  sendIntent.putExtra(Intent.EXTRA_TEXT, message);
    	  startActivity(sendIntent);
      default:
        break;
    }

    // OutSide Switch and case

    // showTextViews function
    showTextViews();

  }

  private void gameStart() {

    // Opening 1 Card of Dealer
    dealerCall();
    calculateDealerScore();

    // Opening 2 Cards of Player
    playerCall();

    // Opening 2 Cards of Player
    playerCall();
    calculatePlayerScore();

    //Check for Split
    if (_playerCardArray[0] == _playerCardArray[1] && _money > _bet) {
      splitAlertBox();
    }
    // Looking for BlackJack
    if (_playerScore == 21) {
      if ((_playerCardArray[0] == 'A' && _playerCardArray[1] == 'J')
          || ((_playerCardArray[0] == 'J' && _playerCardArray[1] == 'A'))) {
        blackJack();
      }
    }
  }

  private void showTextViews() {

    tvMoney.setText(" $ " + _money);
    tvBet.setText("Bet - $ " + _bet);
    tvHighestScore.setText("Highest : " + _highestScore);
    tvDealerScore.setText("Dealer's Score : " + _dealerScore);
    tvYourScore.setText("Your Score : " + _playerScore);

  }

  private void hidePlayButtons() {

    btnHit.setVisibility(View.INVISIBLE);
    btnStand.setVisibility(View.INVISIBLE);
    btnSurrender.setVisibility(View.INVISIBLE);

    sbBetAmount.setVisibility(View.VISIBLE);
  }

  private void showPlayButtons() {

    btnHit.setVisibility(View.VISIBLE);
    btnStand.setVisibility(View.VISIBLE);
    btnSurrender.setVisibility(View.VISIBLE);

    sbBetAmount.setVisibility(View.INVISIBLE);
  }

  public char cardsCalling(int cardNumberFromRandom, ImageSwitcher imageView) {

    // Making some sound here
    {
      SharedPreferences prefs = PreferenceManager
          .getDefaultSharedPreferences(getApplicationContext());
      boolean gameSound = prefs.getBoolean("pref_cb_sound", true);

      if (gameSound) {
        mp = MediaPlayer
            .create(MainActivity.this, R.raw.card_open);
        mp.start();
      }
    }
    // Club,Diamond,Hearts,Spades Sequence--(c,d,h,s)
    // Number sequence A,2,3,4,5,6,7,8,9,10,J,Q,K
    // Card Names : A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, 2

    switch (cardNumberFromRandom) {
      case 0:
        imageView.setImageResource(R.drawable.c1);
        return 'A';
      case 1:
        imageView.setImageResource(R.drawable.c2);
        return '2';
      case 2:
        imageView.setImageResource(R.drawable.c3);
        return '3';
      case 3:
        imageView.setImageResource(R.drawable.c4);
        return '4';
      case 4:
        imageView.setImageResource(R.drawable.c5);
        return '5';
      case 5:
        imageView.setImageResource(R.drawable.c6);
        return '6';
      case 6:
        imageView.setImageResource(R.drawable.c7);
        return '7';
      case 7:
        imageView.setImageResource(R.drawable.c8);
        return '8';
      case 8:
        imageView.setImageResource(R.drawable.c9);
        return '9';
      case 9:
        imageView.setImageResource(R.drawable.c10);
        return 'T';
      case 10:
        imageView.setImageResource(R.drawable.cj);
        return 'J';
      case 11:
        imageView.setImageResource(R.drawable.cq);
        return 'Q';
      case 12:
        imageView.setImageResource(R.drawable.ck);
        return 'K';

      // Diamonds

      case 13:
        imageView.setImageResource(R.drawable.d1);
        return 'A';
      case 14:
        imageView.setImageResource(R.drawable.d2);
        return '2';
      case 15:
        imageView.setImageResource(R.drawable.d3);
        return '3';
      case 16:
        imageView.setImageResource(R.drawable.d4);
        return '4';
      case 17:
        imageView.setImageResource(R.drawable.d5);
        return '5';
      case 18:
        imageView.setImageResource(R.drawable.d6);
        return '6';
      case 19:
        imageView.setImageResource(R.drawable.d7);
        return '7';
      case 20:
        imageView.setImageResource(R.drawable.d8);
        return '8';
      case 21:
        imageView.setImageResource(R.drawable.d9);
        return '9';
      case 22:
        imageView.setImageResource(R.drawable.d10);
        return 'T';
      case 23:
        imageView.setImageResource(R.drawable.dj);
        return 'J';
      case 24:
        imageView.setImageResource(R.drawable.dq);
        return 'Q';
      case 25:
        imageView.setImageResource(R.drawable.dk);
        return 'K';

      // Hearts

      case 26:
        imageView.setImageResource(R.drawable.h1);
        return 'A';
      case 27:
        imageView.setImageResource(R.drawable.h2);
        return '2';
      case 28:
        imageView.setImageResource(R.drawable.h3);
        return '3';
      case 29:
        imageView.setImageResource(R.drawable.h4);
        return '4';
      case 30:
        imageView.setImageResource(R.drawable.h5);
        return '5';
      case 31:
        imageView.setImageResource(R.drawable.h6);
        return '6';
      case 32:
        imageView.setImageResource(R.drawable.h7);
        return '7';
      case 33:
        imageView.setImageResource(R.drawable.h8);
        return '8';
      case 34:
        imageView.setImageResource(R.drawable.h9);
        return '9';
      case 35:
        imageView.setImageResource(R.drawable.h10);
        return 'T';
      case 36:
        imageView.setImageResource(R.drawable.hj);
        return 'J';
      case 37:
        imageView.setImageResource(R.drawable.hq);
        return 'Q';
      case 38:
        imageView.setImageResource(R.drawable.hk);
        return 'K';

      // Spades

      case 39:
        imageView.setImageResource(R.drawable.s1);
        return 'A';
      case 40:
        imageView.setImageResource(R.drawable.s2);
        return '2';
      case 41:
        imageView.setImageResource(R.drawable.s3);
        return '3';
      case 42:
        imageView.setImageResource(R.drawable.s4);
        return '4';
      case 43:
        imageView.setImageResource(R.drawable.s5);
        return '5';
      case 44:
        imageView.setImageResource(R.drawable.s6);
        return '6';
      case 45:
        imageView.setImageResource(R.drawable.s7);
        return '7';
      case 46:
        imageView.setImageResource(R.drawable.s8);
        return '8';
      case 47:
        imageView.setImageResource(R.drawable.s9);
        return '9';
      case 48:
        imageView.setImageResource(R.drawable.s10);
        return 'T';
      case 49:
        imageView.setImageResource(R.drawable.sj);
        return 'J';
      case 50:
        imageView.setImageResource(R.drawable.sq);
        return 'Q';
      case 51:
        imageView.setImageResource(R.drawable.sk);
        return 'K';

      default:

        return 0;
    }

  }

  private int dealerNumber;
  public void dealerCall() {

    // To make sure no card comes twice
    do {
      Random _random = new Random();
      _randomNumber = _random.nextInt(52);
    } while (_alCardsTracking.contains(_randomNumber));
    _alCardsTracking.add(_randomNumber);

    switch (_dealerCardNumber) {
      case 0:
        _dealerCardArray[_dealerCardNumber] = cardsCalling(_randomNumber,
                                                           ivDealerCard1);
        break;
      case 1:
        _dealerCardArray[_dealerCardNumber] = cardsCalling(_randomNumber,
                                                           ivDealerCard2);
        break;
      case 2:
        _dealerCardArray[_dealerCardNumber] = cardsCalling(_randomNumber,
                                                           ivDealerCard3);
        break;
      case 3:
        _dealerCardArray[_dealerCardNumber] = cardsCalling(_randomNumber,
                                                           ivDealerCard4);
        break;
      case 4:
        _dealerCardArray[_dealerCardNumber] = cardsCalling(_randomNumber,
                                                           ivDealerCard5);
        break;
    }
    // Very important
    dealerNumber = _randomNumber;
    _dealerScoreCount[_dealerCardNumber] = getIntValueFromCard(_dealerCardArray[_dealerCardNumber]);
    _dealerCardNumber++;

  }

  public void playerCall() {

    // To make sure no card comes twice
    do {
      Random _random = new Random();
      _randomNumber = _random.nextInt(52);
    } while (_alCardsTracking.contains(_randomNumber));
    _alCardsTracking.add(_randomNumber);

    switch (_playerCardNumber) {
      case 0:
        _playerCardArray[_playerCardNumber] = cardsCalling(_randomNumber,
                                                           ivYourCard1);
        break;
      case 1:
        _playerCardArray[_playerCardNumber] = cardsCalling(_randomNumber,
                                                           ivYourCard2);
        break;
      case 2:
        _playerCardArray[_playerCardNumber] = cardsCalling(_randomNumber,
                                                           ivYourCard3);
        break;
      case 3:
        _playerCardArray[_playerCardNumber] = cardsCalling(_randomNumber,
                                                           ivYourCard4);
        break;
      case 4:
        _playerCardArray[_playerCardNumber] = cardsCalling(_randomNumber,
                                                           ivYourCard5);
        break;
    }
    // Very Important
    _playerScoreCount[_playerCardNumber] = getIntValueFromCard(_playerCardArray[_playerCardNumber]);
    _playerCardNumber++;
  }

  public void calculateDealerScore() {

    int j = 0;
    for (int i = 0; i < 5; i++) {
      j += _dealerScoreCount[i];
    }
    _dealerScore = j;
    tvDealerScore.setText("Dealer's Score : " + _dealerScore);
  }

  public void calculatePlayerScore() {

    int j = 0;
    for (int i = 0; i < 5; i++) {
      j += _playerScoreCount[i];
    }
    _playerScore = j;
    tvYourScore.setText("Your Score : " + _playerScore);

  }

  public void youLose() {
    _bet = 0;
    Toast.makeText(MainActivity.this, "You Lose !!!",
                   Toast.LENGTH_LONG).show();

    // Making some sound here
    SharedPreferences prefs = PreferenceManager
        .getDefaultSharedPreferences(getApplicationContext());
    boolean gameSound = prefs.getBoolean("pref_cb_sound", true);

    if (gameSound) {
      mp = MediaPlayer.create(MainActivity.this, R.raw.you_lose);
      mp.start();
    }
  }

  public void youWon() {
    _money += _bet * 2;

    _bet = 0;
    Toast.makeText(MainActivity.this, "You Won !!!",
                   Toast.LENGTH_SHORT).show();
    // Making some sound here
    {
      SharedPreferences prefs = PreferenceManager
          .getDefaultSharedPreferences(getApplicationContext());
      boolean gameSound = prefs.getBoolean("pref_cb_sound", true);

      if (gameSound) {
        mp = MediaPlayer.create(MainActivity.this, R.raw.you_won);
        mp.start();
      }
    }
  }

  public void blackJack() {
    _money += _bet * 3;
    _bet = 0;
    Toast.makeText(MainActivity.this, "Congrats! You Hit BLACKJACK",
                   Toast.LENGTH_LONG).show();
    alertBox();
  }

  public int getIntValueFromCard(char card) {

    switch (card) {
      case 'A':
        return 11;
      case 'K':
        return 10;
      case 'Q':
        return 10;
      case 'J':
        return 10;
      case 'T':
        return 10;
      case '9':
        return 9;
      case '8':
        return 8;
      case '7':
        return 7;
      case '6':
        return 6;
      case '5':
        return 5;
      case '4':
        return 4;
      case '3':
        return 3;
      case '2':
        return 2;
      default:
        return 0;

    }
  }

  private void btnHitClick() {
    playerCall();
    calculatePlayerScore();

    // Aces count as 1
    if (_playerScore > 21) {
      for (int i = 0; i < 5; i++) {
        if (_playerCardArray[i] == 'A' && _playerScoreCount[i] == 11) {
          _playerScoreCount[i] = 1;
          Toast.makeText(MainActivity.this,
                         "Aces will be count as 1", Toast.LENGTH_LONG)
              .show();
          break;
        }
      }
      calculatePlayerScore();
    }

    // If reached here player has no aces
    if (_playerScore > 21) {
      youLose();
      showTextViews();

      Thread t = new Thread() {
        public void run() {
          runOnUiThread(new Runnable() {
            public void run() {
              try {
                sleep(700);
              } catch (InterruptedException e) {
              }
              alertBox();
            }
          });
        }
      };
      t.start();
    }

    if (_playerCardNumber >= 5 && _playerScore < 22) {
      //
      btnStandClick();
      alertBox();
    }

  }

  public void btnStandClick() {
    // Computer Play here
    do {
      // Opening 1 Card of Dealer
      dealerCall();
      calculateDealerScore();

      // Aces count as 1
      if (_dealerScore > 21) {
        for (int i = 0; i < 5; i++) {
          if (_dealerCardArray[i] == 'A'
              && _dealerScoreCount[i] == 11) {
            _dealerScoreCount[i] = 1;
            break;
          }
        }
        calculateDealerScore();
      }

    } while (_dealerScore < 17 && _dealerScore <= _playerScore
             && _dealerCardNumber < 5);

    if (_dealerScore > 21) {
      youWon();
    } else {
      checkWin();
    }
    if (_dealerCardNumber >= 5) {
      checkWin();
    }

  }

  public void checkWin() {
    if (_dealerScore > _playerScore) {
      youLose();

    } else if (_playerScore > _dealerScore) {
      youWon();

    } else {
      _money += _bet;
      _bet = 0;
      Toast.makeText(MainActivity.this, "Game Drawn!!!",
                     Toast.LENGTH_SHORT).show();

    }
  }

  public void splitAlertBox() {

    final Builder alert = new Builder(this);
    alert.setCancelable(false);
    Thread t = new Thread() {
      public void run() {

        runOnUiThread(new Runnable() {
          public void run() {
            alert.setMessage("Would you like to split for double your bet?");
            alert.setPositiveButton("Yes Please",
                                    new DialogInterface.OnClickListener() {

                                      public void onClick(DialogInterface dialog,
                                                          int which) {
                                        SplitClicked();
                                      }
                                    });
            alert.show();
          }
        });
        alert.setNegativeButton("No Thanks", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int arg1) {
            dialog.dismiss();
          }
        });
      }
    };
    t.start();
    // Thread ends here
  }

  private void SplitClicked() {
    splitting = true;
    _playerScore = _playerScore - _playerScoreCount[1];
    tvYourScore.setText("Your Score : " + _playerScore);

    ivSplitCard1.setVisibility(View.VISIBLE);
    ivSplitCard2.setVisibility(View.VISIBLE);
    ivSplitCard3.setVisibility(View.VISIBLE);
    ivSplitCard4.setVisibility(View.VISIBLE);
    ivSplitCard5.setVisibility(View.VISIBLE);

    _playerCardNumber--;
    _splitBet = _bet;
    tvMoney.setText(" $ " + _money);
    tvBet.setText("Bet - $ " + _bet);

    //========================

    ivSplitCard1.setInAnimation(AnimationUtils.loadAnimation(getBaseContext(),
                                                             android.R.anim.slide_in_left));
    ivSplitCard1.setOutAnimation(AnimationUtils.loadAnimation(getBaseContext(),
                                                              android.R.anim.slide_out_right));

    ivSplitCard2.setInAnimation(AnimationUtils.loadAnimation(getBaseContext(),
                                                             android.R.anim.slide_in_left));
    ivSplitCard2.setOutAnimation(AnimationUtils.loadAnimation(getBaseContext(),
                                                              android.R.anim.slide_out_right));

    ivSplitCard3.setInAnimation(AnimationUtils.loadAnimation(getBaseContext(),
                                                             android.R.anim.slide_in_left));
    ivSplitCard3.setOutAnimation(AnimationUtils.loadAnimation(getBaseContext(),
                                                              android.R.anim.slide_out_right));

    ivSplitCard4.setInAnimation(AnimationUtils.loadAnimation(getBaseContext(),
                                                             android.R.anim.slide_in_left));
    ivSplitCard4.setOutAnimation(AnimationUtils.loadAnimation(getBaseContext(),
                                                              android.R.anim.slide_out_right));

    ivSplitCard5.setInAnimation(AnimationUtils.loadAnimation(getBaseContext(),
                                                             android.R.anim.slide_in_left));
    ivSplitCard5.setOutAnimation(AnimationUtils.loadAnimation(getBaseContext(),
                                                              android.R.anim.slide_out_right));
    ivSplitCard1.setImageResource(R.drawable.default_blue);
    ivSplitCard2.setImageResource(R.drawable.default_blue);
    ivSplitCard3.setImageResource(R.drawable.default_blue);
    ivSplitCard4.setImageResource(R.drawable.default_blue);
    ivSplitCard5.setImageResource(R.drawable.default_blue);

    Thread t = new Thread() {
      public void run() {

        try {
          sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    };
    t.start();

    ivYourCard2.setImageResource(R.drawable.default_blue);
    _splitCard = _randomNumber;
    _splitDealerCard = dealerNumber;
   _splitScore = _playerScore;
    cardsCalling(_splitCard, ivSplitCard1);
  }

  public void alertBox() {

    final Builder alert = new Builder(this);
    alert.setCancelable(false);
    String message = "Play Again";
    String positive = "Next Hand";
    if (splitting) {
      message = "You previously split, continue?";
      positive = "I am ready";
    }
    final String finalMessage = message;
    final String finalPositive = positive;
    Thread t = new Thread() {
      public void run() {

        try {
          sleep(1500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        runOnUiThread(new Runnable() {
          public void run() {
            alert.setMessage(finalMessage);
            alert.setPositiveButton(finalPositive,
                                    new DialogInterface.OnClickListener() {

                                      public void onClick(DialogInterface dialog,
                                                          int which) {
                                        if (splitting) {
                                          _bet = _splitBet;
                                          _money = _money - _bet;
                                          tvMoney.setText(" $ " + _money);
                                          tvBet.setText("Bet - $ " + _bet);

                                          splitting = false;
                                          ivSplitCard1.setVisibility(View.GONE);
                                          ivSplitCard2.setVisibility(View.GONE);
                                          ivSplitCard3.setVisibility(View.GONE);
                                          ivSplitCard4.setVisibility(View.GONE);
                                          ivSplitCard5.setVisibility(View.GONE);

                                          ivDealerCard1.setImageResource(R.drawable.default_red);
                                          ivDealerCard2.setImageResource(R.drawable.default_red);
                                          ivDealerCard3.setImageResource(R.drawable.default_red);
                                          ivDealerCard4.setImageResource(R.drawable.default_red);
                                          ivDealerCard5.setImageResource(R.drawable.default_red);

                                          ivYourCard1.setImageResource(R.drawable.default_blue);
                                          ivYourCard2.setImageResource(R.drawable.default_blue);
                                          ivYourCard3.setImageResource(R.drawable.default_blue);
                                          ivYourCard4.setImageResource(R.drawable.default_blue);
                                          ivYourCard5.setImageResource(R.drawable.default_blue);

                                          for (int i = 0; i < 5; i++) {
                                            _dealerCardArray[i] = '0';
                                            _dealerScoreCount[i] = 0;
                                            _playerCardArray[i] = '0';
                                            _playerScoreCount[i] = 0;
                                          }

                                          _dealerCardArray[0] = cardsCalling(_splitDealerCard, ivDealerCard1);
                                          _dealerScoreCount[0] = getIntValueFromCard(_dealerCardArray[0]);
                                          _playerCardArray[0] = cardsCalling(_splitCard, ivYourCard1);
                                          _playerScoreCount[0] = getIntValueFromCard(_playerCardArray[0]);
                                          _playerCardNumber = 1;
                                          _dealerCardNumber = 1;

                                          calculateDealerScore();
                                          calculatePlayerScore();
                                        }else  {
                                          resetEveryThing();
                                          hidePlayButtons();
                                          // Making some sound here
                                          SharedPreferences prefs = PreferenceManager
                                              .getDefaultSharedPreferences(getApplicationContext());
                                          boolean gameSound = prefs.getBoolean(
                                              "pref_cb_sound",
                                              true);

                                          if (gameSound) {
                                            mp.release();
                                          }
                                        }
                                      }
                                    });
            alert.show();
          }
        });
      }
    };
    t.start();
    // Thread ends here
  }

  public void playAllOverAgainAlertBox() {

    final Builder alert = new Builder(this);
    alert.setCancelable(false);

    Thread t = new Thread() {
      public void run() {

        try {
          sleep(1500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        runOnUiThread(new Runnable() {
          public void run() {
            alert.setMessage("Play Again");
            alert.setPositiveButton("Get more $500 to Play!",
                                    new DialogInterface.OnClickListener() {

                                      public void onClick(DialogInterface dialog,
                                                          int which) {
                                        _money = 500;
                                        resetEveryThing();
                                        hidePlayButtons();
                                      }
                                    });

            alert.setNegativeButton("I'm Sick of this game!",
                                    new DialogInterface.OnClickListener() {

                                      public void onClick(DialogInterface dialog,
                                                          int which) {
                                        savingHighScore();
                                        finish();
                                      }
                                    });

            alert.show();
          }
        });
      }
    };
    t.start();
    // Thread ends here
  }

  // Seekbar methods

  public void onProgressChanged(SeekBar seekBar, int progress,
                                boolean fromUser) {
    try {
      Thread.sleep(15);
    } catch (InterruptedException e) {
    }

    _bet = progress;
    tvBet.setText("Bet - $ " + _bet);
  }

  public void onStartTrackingTouch(SeekBar seekBar) {

  }

  public void onStopTrackingTouch(SeekBar seekBar) {
  }

  public View makeView() {
    ImageView iView = new ImageView(this);
    iView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    iView.setLayoutParams(new ImageSwitcher.LayoutParams(
        LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    return iView;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    MenuInflater menuSetBg = getMenuInflater();
    menuSetBg.inflate(R.menu.menu, menu);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);

    switch (item.getItemId()) {
      case R.id.mISelectBackground:
        Intent intent = new Intent(MainActivity.this, SetBackground.class);
        startActivity(intent);
        break;
      case R.id.mISettings:
        Intent prefIntent = new Intent(MainActivity.this, PreferencesActivity.class);
        startActivity(prefIntent);
        break;
    }
    return true;
  } 
}