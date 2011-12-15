package com.morgan.design.android.util;

import java.util.List;

import android.location.Location;

import com.morgan.design.android.domain.WOEIDEntry;
import com.morgan.design.android.domain.YahooWeatherInfo;
import com.morgan.design.android.domain.types.Temperature;
import com.morgan.design.android.parser.WOIEDParser;
import com.morgan.design.android.parser.YahooWeatherInfoParser;

public class YahooRequestUtils {

	private static final String URL_YAHOO_API_WEATHER = "http://weather.yahooapis.com/forecastrss?w=%s&u=%s";

	private static final String GET_LOCATION_WOEID_FOR_TEXT =
			"http://query.yahooapis.com/v1/public/yql?q=select * from geo.places where text='%s' | SORT(field='placeTypeName.code')&format=xml";

	private static final String GET_LOCATION_WOEID_FOR_LOCATION =
			"http://query.yahooapis.com/v1/public/yql?q=select * from geo.places where text='%s, %s' | SORT(field='placeTypeName.code')&format=xml";

	private static final String LOG_TAG = "YahooRequestLoader";

	private YahooRequestUtils() {
		super();
	}

	private static YahooRequestUtils instance;

	public synchronized static YahooRequestUtils getInstance() {
		if (instance == null) {
			instance = new YahooRequestUtils();
		}
		return instance;
	}

	public YahooWeatherInfo getWeatherInfo(final String result) {
		// Logger.i(LOG_TAG, result);
		return YahooWeatherInfoParser.getInstance().parse(result);
	}

	public List<WOEIDEntry> parseWOIEDResults(final String results) {
		// Logger.i(LOG_TAG, results);
		return WOIEDParser.getInstance().parse(results);
	}

	public String createQuerryGetWoeid(final String strQuerry) {
		if (strQuerry == null) {
			return null;
		}
		return String.format(GET_LOCATION_WOEID_FOR_TEXT, strQuerry);
	}

	// http://stackoverflow.com/questions/2502912/using-latitude-longitude-to-get-a-place-with-yahoo
	public String createQuerryGetWoeid(final Location location) {
		if (location == null) {
			return null;
		}
		return String.format(GET_LOCATION_WOEID_FOR_LOCATION, location.getLatitude(), location.getLongitude());
	}

	// http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.placefinder%20where%20text%3D%2237.416275%2C%20-122.025092%22%20and%20gflags%3D%22R%22&appid=test

	public String createWeatherQuery(final WOEIDEntry woiedEntry, final Temperature temperature) {
		if (woiedEntry == null) {
			return null;
		}
		return createWeatherQuery(woiedEntry.getWoeid(), temperature);
	}

	public String createWeatherQuery(final String woeidId, final Temperature temperature) {
		if (woeidId == null) {
			return null;
		}
		return String.format(URL_YAHOO_API_WEATHER, woeidId, temperature.getAbrev());
	}
}