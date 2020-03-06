//MARIN KOCOLLARI

import java.util.*;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.Queue;
import java.io.*;
import java.io.File;

//linked list structure
class MyQueue extends LinkedList{
	protected Song first;
	
	static class Song{
		String track;
		Song next;
	
		Song(String track){
			this.track=track;
			next = null;
		}
	}
	//add song
	public static MyQueue insert(MyQueue queue, Song song){
		Song new_song = song;
		new_song.next = null;
		
		if(queue.first==null){
			queue.first = new_song;
		}
		else {
			Song last = queue.first;
			while(last.next != null){
				last = last.next;
			}
			last.next = new_song;
		}
		return queue;
	}
	//add song (string)
	public static MyQueue insert(MyQueue queue, String song){
	Song new_song = new Song(song);
	new_song.next = null;	
	if(queue.first==null){
		queue.first = new_song;
	}
	else {
		Song last = queue.first;
		while(last.next != null){
			last = last.next;
		}
		last.next = new_song;
	}
	return queue;
	}
	//prints linkedlist
	public static void printQueue(MyQueue queue, PrintStream output){
		Song currSong = queue.first;
		while(currSong!=null){
			output.println(currSong.track);
			currSong=currSong.next;
		}
	}
	//merges sorted linked lists
	public Song sortedMerge(Song a, Song b){
		Song result = null;
		
		if(a == null){
			return b;
		}
		if(b == null){
			return a;
		}
		if(a.track.compareToIgnoreCase(b.track)<=0){
			result = a;
			result.next = sortedMerge(a.next, b);
		}
		else{
			result = b;
			result.next = sortedMerge(a, b.next);
		}
		return result;
	}
	//sorts linked list
	public Song mergeSort(Song h){
		if(h == null || h.next == null){
			return h;
		}
		
		Song mid = getMid(h);
		Song nextMid = mid.next;
		mid.next = null;
		Song left = mergeSort(h);
		Song right = mergeSort(nextMid);
		Song sortedList = sortedMerge(left, right);
		return sortedList;
	}
	//method helpful in sorting linkedlist
	public static Song getMid(Song h){
		if(h == null){
			return h;
		}
		Song slow = h;
		Song fast = h;
		while(fast.next != null && fast.next.next != null){
			slow = slow.next;
			fast = fast.next.next;
		}
		return slow;
	}
	//reverses the sort
	public Song reverse(Song song){
		Song previous = null;
		Song current = song;
		Song next = null;
		while(current != null){
			next = current.next;
			current.next= previous;
			previous= current;
			current = next;
		}
		song = previous;
		return song;
	}
	//removes duplicates
	public void removeDuplicates(){
		Song current = first;
		while(current != null){
			Song temp = current;
			while(temp != null && temp.track.equalsIgnoreCase(current.track)){
				temp = temp.next;
			}
			current.next = temp;
			current = current.next;
		}
	}
	//returns song and adds it to stack of last listened songs
	public Song listenToSong(Stack<Song> stack){
		stack.stack_push(first.next);
		return first.next;
	}
}
class Main{
	public static void main(String[] args) throws Exception{
		//reads in files
		String[] myFiles = new String[]{"./data/week1.csv","./data/week2.csv","./data/week3.csv","./data/week4.csv","./data/week5.csv"};
		MyQueue[] allTheWeeks = new MyQueue[5];
		PrintStream output = new PrintStream(new File("output.txt"));
		//files parsed through
		for(int i=0;i<=4;i++){
			allTheWeeks[i] = new MyQueue();
			File temp = new File(myFiles[i]);
			BufferedReader stdin = new BufferedReader(new FileReader(temp));
			ArrayList<String[]> records = new ArrayList<String[]>();
			String[] record = new String[3];
			//lines parsed, array of lines created
			while(stdin.readLine() != null){
				record = stdin.readLine().split(",");
				records.add(record);
			}
			String[] songs = new String[records.size()];
			//songs inserted in linked list structure
			for(int n = 1; n < records.size(); n++){
				allTheWeeks[i].insert(allTheWeeks[i], records.get(n)[1].replaceAll("\"",""));
			}
			//linked lists sorted (descending)
			allTheWeeks[i].mergeSort(allTheWeeks[i].first);
		}
		//last listened song history stack announced
		Stack<Song> songhistory = new Stack<Song>();
		//playlist with all tracks announced
		MyQueue playlist = new MyQueue();
		//weekly charts merged to playlist
		playlist.first = playlist.sortedMerge(allTheWeeks[0].first, allTheWeeks[1].first);
		playlist.first = playlist.sortedMerge(playlist.first, allTheWeeks[2].first);
		playlist.first = playlist.sortedMerge(playlist.first, allTheWeeks[3].first);
		playlist.first = playlist.sortedMerge(playlist.first, allTheWeeks[4].first);
		//duplicate tracks removed
		playlist.removeDuplicates();
		//playlist sorted in ascending order
		playlist.first = playlist.reverse(playlist.first);
		//listens to next song and adds it to history stack
		playlist.listenToSong(songhistory);
		//prints out the playlist
		playlist.printQueue(playlist);
	}
}
