// Copyright 2016 Twitter. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.twitter.heron.examples;

import java.time.Duration;
import java.util.Map;
import java.util.Random;

import com.twitter.heron.common.basics.SysUtils;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class TestWordSpout extends BaseRichSpout {

  private static final long serialVersionUID = -3217886193225455451L;
  private SpoutOutputCollector collector;
  private String[] words;
  private Random rand;
  private final Duration throttleDuration;

  public TestWordSpout() {
    this(Duration.ZERO);
  }

  public TestWordSpout(Duration throttleDuration) {
    this.throttleDuration = throttleDuration;
  }

  @SuppressWarnings("rawtypes")
  public void open(
      Map conf,
      TopologyContext context,
      SpoutOutputCollector acollector) {
    collector = acollector;
    words = new String[]{"nathan", "mike", "jackson", "golda", "bertels"};
    rand = new Random();
  }

  public void close() {
  }

  public void nextTuple() {
    final String word = words[rand.nextInt(words.length)];
    collector.emit(new Values(word));
    if (!throttleDuration.isZero()) {
      SysUtils.sleep(throttleDuration); // sleep to throttle back cpu usage
    }
  }

  public void ack(Object msgId) {
  }

  public void fail(Object msgId) {
  }

  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields("word"));
  }
}
