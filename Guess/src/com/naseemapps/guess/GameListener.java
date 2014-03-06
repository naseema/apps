package com.naseemapps.guess;


public interface GameListener {

	abstract public void onSuccess();

	abstract public void onFail();
	
	abstract public void onUsingHelpRemoveLetter();
	
	abstract public void onUsingHelpRevealLetter();
	
	abstract public GameActivity.UserPlayerEntity getUserEntity();

	abstract public void noEnoughConis();

}
