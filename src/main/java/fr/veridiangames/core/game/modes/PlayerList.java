package fr.veridiangames.core.game.modes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jimi Vacarians on 31/08/2017.
 */
public class PlayerList {
	private List<Integer> list = new ArrayList<>();

	public boolean contains(int id){
		for(int i : list){
			if(i == id){
				return true;
			}
		}
		return false;
	}

	public void add(int id){
		if(!contains(id)){
			list.add(id);
		}
	}

	public boolean remove(int id){
		for(int i = 0; i < list.size(); i++)
		{
			int cid = list.get(i);
			if (cid == id)
			{
				list.remove(i);
			}
			return true;
		}

		return false;
	}

	public int getSize(){
		return list.size();
	}

	public void clear(){
		list.clear();
	}

	public List<Integer> getList(){
		return list;
	}
}
