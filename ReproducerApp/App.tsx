import React, { useEffect, useState } from 'react';
import { NativeModules, Platform, StyleSheet, Text, View } from 'react-native';

function createBlockingDelay(milliseconds: number) {
  return new Promise((resolve) => {
    const startTime = Date.now();
    while (Date.now() - startTime < milliseconds) {
      // Busy-waiting loop to simulate synchronous delay
    }
    resolve(true);
  });
}

function App() {
  const [counter, setCounter] = useState(0);

  useEffect(() => {
    createBlockingDelay(1000).then(() => {
      setCounter((previousCount) => previousCount + 1);
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
