# ec2-ri-optimizer
Sumo Logic's Amazon Web Services EC2 Reservations optimizing tool which:
* analize your reserved instances
* points out over and underreserved EC2 instances classes
* suggests possible conversions which could lower your reservations overall costs `$$$` assuming that all reservations
have similar unit cost ($/month) and no-upfront. More about AWS pricing @ https://aws.amazon.com/ec2/pricing/reserved-instances/pricing/

## Install / Download
These are the components we provide:
* `ec2ri-optimizer` contains tools for downloading and analyzing reserved EC2 instances on AWM

## Using as library
You can include this project and use as library in any way you like.
Main class is `Ec2RiOptimizer` with input argument `accountsFilename` which is path to CSV file with AWS accounts
(IAM key/secret pairs) you would like to analize:
```
<aws_region>;<aws_key1>;<aws_secret1>
<aws_region>;<aws_key2>;<aws_secret2>
<aws_region>;<aws_key3>;<aws_secret3>
...
<aws_region>;<aws_key_n>;<aws_secret_n>
```
`Ec2RiOptimizer` has method `analize` wich produces `Ec2RiAnalysis` containing information abaout over/underreserved
instances and possible conversions.

## Run
Optimizer has shell command. You just run the class `Ec2RiShell` and type `help` for more info.

## Warning
Remember that this is only a TOOL that could help you and SUGGESTS conversions. It doesn't actually:
* convert reservetions for you (this is planned for future)
* guearantee that this will cut your AWS costs!
Please check the results before applying them to your business