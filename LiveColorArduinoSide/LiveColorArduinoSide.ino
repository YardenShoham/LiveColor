int redPin = 11;
int bluePin = 10;
int greenPin = 9;

String serialData;

void setup()
{
	pinMode(redPin, OUTPUT);
	pinMode(bluePin, OUTPUT);
	pinMode(greenPin, OUTPUT);
	digitalWrite(bluePin, HIGH);
	digitalWrite(redPin, HIGH);
	digitalWrite(greenPin, HIGH);
	Serial.begin(9600);
	Serial.setTimeout(15);
}

void loop()
{}

void color (unsigned char red, unsigned char green, unsigned char blue)// the color generating function
{
  analogWrite(redPin, 255 - red);   // PWM signal output
  analogWrite(greenPin, 255 - green); // PWM signal output
  analogWrite(bluePin, 255 - blue); // PWM signal output
}

void serialEvent()
{
	serialData = Serial.readString(); // example: R65G120B4
	color(getRed(serialData), getGreen(serialData), getBlue(serialData));
}

int getRed(String data)
{
	data.remove(data.indexOf("G"));
	data.remove(0, 1);
	return data.toInt();
}

int getGreen(String data)
{
	data.remove(data.indexOf("B"));
	data.remove(0, data.indexOf("G") + 1);
	return data.toInt();
}

int getBlue(String data)
{
	data.remove(0, data.indexOf("B") + 1);
	return data.toInt();
}
