<template>
<table border="1px" width="100%" style="border-collapse: collapse;border-radius: 10%">
    <tr style="background-color:#79bbff; color:#fff">
      <th>课程节次</th>
      <th>周一</th>
      <th>周二</th>
      <th>周三</th>
      <th>周四</th>
      <th>周五</th>
    </tr>
    <tr v-for="classCount in classNum" style="height: 10%">
      <td>
        第{{classCount}}节课
        <br>
        {{classInfo[classCount-1].startTime+'~'+classInfo[classCount-1].endTime}}
      </td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
    </tr>
  </table>
</template>

<script>
export default {
  name: "ClassScheduleTable",
  props:{
    classNum:Number,
    classInfo:Object
  },
}
</script>

<style scoped>

</style>
