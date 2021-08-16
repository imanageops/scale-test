#!/usr/bin/env bash
for sc in SingleTermEsQuery ThreeTermsEsQuery WildCardLeadingSingleTermEsQuery WildCardLeadingThreeTermsEsQuery WildCardTrailingSingleTermEsQuery WildCardTrailingThreeTermsEsQuery
do
  echo "Running test scenario: "${sc}
  ./gatling-charts-highcharts-bundle-3.6.1/scripts/run-es-contant-duration.sh ${sc}
  echo "Finished test scenario: "${sc}
done
