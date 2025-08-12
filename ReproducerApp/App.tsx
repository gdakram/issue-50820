import React, { useEffect, useState } from 'react';
import { NativeModules, Platform, StyleSheet, Text, View } from 'react-native';

function createBlockingDelay(milliseconds: number) {
  return new Promise(resolve => {
    const startTime = Date.now();
    while (Date.now() - startTime < milliseconds) {
      // Busy-waiting loop to simulate synchronous delay
    }
    fetch('https://www.nfl.com/?headless-promise').then(() => resolve(true));
  });
}

function App() {
  const [counter, setCounter] = useState(0);

  useEffect(() => {
    const intervalId = setInterval(() => {
      fetch('https://www.nfl.com/?headless-interval').catch(error => {
        console.error('Fetch error:', error);
      });
    }, 2000);

    return () => clearInterval(intervalId);
  }, []);

  useEffect(() => {
    createBlockingDelay(2000).then(() => {
      setCounter(previousCount => previousCount + 1);
    });
  }, [counter]);

  useEffect(() => {
    // needs to be called before PIP is entered
    if (Platform.OS === 'android' && NativeModules.HeadlessJSModule) {
      NativeModules.HeadlessJSModule.updateContext();
    }
  }, []);

  return (
    <View style={styles.container}>
      <Text style={styles.counterText}>{counter}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  counterText: {
    fontSize: 50,
  },
});

export default App;
