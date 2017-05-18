/**
 * Dependancies: Jsoup & URISchemeHandler  !!
 */

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import uriSchemeHandler.CouldNotOpenUriSchemeHandler;
import uriSchemeHandler.URISchemeHandler;

public class Crawler{
	public static void main(String[] args) {
		try {
			Scanner stdin = new Scanner(System.in);
			System.out.println("Disclaimer: Downloading copyrighted materials is not a purpose of this program ... ");
			System.out.println();
			System.out.println("What are we watching? ");
			System.out.println();
			String query = stdin.nextLine();
			System.out.println();
			String[] queryArray = query.split(" ");
			//build query
			query = "";
			for(String str : queryArray) {
				query += str;
				query += "%20";
			}
			if(queryArray.length > 1) {			
				query = query.substring(0, query.length()-3);
			}
			

			Document doc = Jsoup.connect("https://thepiratebay.org/search/"+query+"/0/99/200").userAgent("Mozilla")
					.ignoreHttpErrors(true).timeout(0).get();
			Elements elems = doc.getElementsByClass("detName").select("a");
			elems.toArray();

			for (int i = 0; i < elems.size() / 2; i++) {
				System.out.println(i + 1 + ". " + elems.get(i).text());
			}
			if(elems.isEmpty()) {
				System.out.println("No records");
				System.exit(0);
			}
			System.out.println();
			System.out.println("What are we downloading today?");
			
			int index = stdin.nextInt();
			System.out.println();
			System.out.println("Opening default torrent client...");
			Document chosen = Jsoup.connect("http://www.thepiratebay.org" + elems.get(index - 1).attr("href"))
					.userAgent("Mozilla").ignoreHttpErrors(true).timeout(0).get();
			String magnet = chosen.getElementsByClass("download").select("a").attr("href");
			try {
				URI magnetLinkUri = new URI(magnet);
				URISchemeHandler uriSH = new URISchemeHandler();
				try {
					uriSH.open(magnetLinkUri);
				} catch (CouldNotOpenUriSchemeHandler e) {
					System.out.println("ERROR OPENING MAGNET LINK. YOU'RE DONE SON!");
				}
			} catch (URISyntaxException e) {
				System.out.println("ERROR: " + e);
			}
		} catch (IOException e) {
			System.out.println("ERROR: " + e);
		}
	}
}
