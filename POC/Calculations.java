 
public class Calculations {
	
	public Positions  evaluate(int p1, int p2, int p3, int p4, int d1, int d2,int angle)
	{
		boolean isHarmful = false;
		String suggestion = "You are doing good. Keep it up";
		String position = "Correct Position";
		/* Leaning Back*/
		if(p1 == 1 && p2 == 0)
		{
			if(d2 <= 5)
			{
				isHarmful = true;
				position = "You are Leaning Back";
				suggestion ="Please straighten your posture";
			}
			else if(d2 >=10)
			{
				isHarmful = true;
				position = "You are Sitting the edge";
				
				suggestion ="Please try to your back";				
			}
		}
		/*Leaning Front*/
		else if(p1 == 1 && p2 == 1)
		{
			if(d2 >= 10)
			{
				isHarmful = true;
				position = "You are Leaning Front";
				suggestion ="Please straighten your posture";
			}
			else if(d2<=5)
			{
				isHarmful = false;
				position = "You are with the chair";
				suggestion ="This a good posture";				
			}
		}
		
		if(angle >=90 && angle <=95)
		{
			isHarmful = false;
			position = "Reclining well";
			suggestion = "This is a good posture to relax";
		}
		
		if(angle >95)
		{
			isHarmful = true;
			position = "Reclining too much";
			suggestion = "Please go back to normal postion";

		}
		
		
		Positions pos = new Positions();
		pos.setHarmful(isHarmful);
		pos.setPosition(position);
		pos.setSuggestion(suggestion);
	/*	
	  String isHarmfulString  = isHarmful?"True":"False"; 
	 String json = "{";
		json += "\"isharmful\" :\"";
		json += isHarmfulString +"\"";
		json += "\"position\" :\"";
		json += position +"\"";
		json += "\"suggestion\" :\"";
		json += suggestion +"\"";
		json += "}";
		*/		
		
		return pos;
	}
}
