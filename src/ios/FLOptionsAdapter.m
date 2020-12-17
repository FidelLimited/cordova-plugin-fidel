#import "FLOptionsAdapter.h"
#if __has_include(<Fidel/Fidel-Swift.h>)
#import <Fidel/Fidel-Swift.h>
#elif __has_include("Fidel-Swift.h")
#import "Fidel-Swift.h"
#else
#import "Fidel/Fidel-Swift.h" // Required when used as a Pod in a Swift project
#endif
#import "FLCardSchemesAdapter.h"


@interface FLOptionsAdapter()

@property (nonatomic, strong) id<FLCardSchemesAdapter> cardSchemesAdapter;

@end

@implementation FLOptionsAdapter


-(instancetype)initWithcardSchemesAdapter:(id<FLCardSchemesAdapter>)cardSchemesAdapter {
    
    self = [super init];
    if (self) {
        _cardSchemesAdapter = cardSchemesAdapter;
    }
    return self;
}

NSString *const kCountryKey = @"Country";
NSString *const kOptionKey = @"Option";
NSString *const kBannerImageOptionKey = @"bannerImageName";
NSString *const kCountryOptionKey = @"country";
NSString *const kAutoScanOptionKey = @"autoScan";
NSString *const kCompanyNameOptionKey = @"companyName";
NSString *const kMetaDataOptionKey = @"metaData";
NSString *const kCardSchemesDataOptionKey = @"supportedCardSchemes";
NSString *const kProgramNameOptionKey = @"programName";
NSString *const kDeleteInstructionsOptionKey = @"deleteInstructions";
NSString *const kPrivacyURLOptionKey = @"privacyUrl";
NSString *const kTermsConditionsURLOptionKey = @"termsConditionsUrl";


- (void)setOptions:(NSDictionary *)options {
    if ([self valueIsValidFor:kBannerImageOptionKey fromDictionary:options]) {
        NSString *bannerImageName = [self getStringValueFor:kBannerImageOptionKey fromDictionary:options];
        UIImage *bannerImage = [UIImage imageNamed:bannerImageName];
        [FLFidel setBannerImage:bannerImage];
    }
    
    if ([self valueIsValidFor:kCountryOptionKey fromDictionary:options]) {
        id rawCountry = options[kCountryOptionKey];
        NSNumber *rawValue = rawCountry;
        FLFidel.country = (FLCountry) rawValue.intValue;
    }
    
    if ([self valueIsValidFor:kAutoScanOptionKey fromDictionary:options]) {
        FLFidel.autoScan = [options[kAutoScanOptionKey] boolValue];
    }
    
    if ([self valueIsValidFor:kMetaDataOptionKey fromDictionary:options]) {
        id rawMetaData = options[kMetaDataOptionKey];
        if ([rawMetaData isKindOfClass:[NSDictionary class]]) {
            FLFidel.metaData = rawMetaData;
        }
    }
    
    if([self valueIsValidFor:kCardSchemesDataOptionKey fromDictionary:options]) {
        id rawData = options[kCardSchemesDataOptionKey];
        NSSet<NSNumber *> *supportedCardSchemes = [self.cardSchemesAdapter cardSchemesWithRawObject:rawData];
        FLFidel.objc_supportedCardSchemes = supportedCardSchemes;
    }
    
    if([self valueIsValidFor:kCompanyNameOptionKey fromDictionary:options]) {
        FLFidel.companyName = [self getStringValueFor:kCompanyNameOptionKey fromDictionary:options];
    }
    
    if([self valueIsValidFor:kProgramNameOptionKey fromDictionary:options]) {
        FLFidel.programName = [self getStringValueFor:kProgramNameOptionKey fromDictionary:options];
    }
    
    if([self valueIsValidFor:kDeleteInstructionsOptionKey fromDictionary:options]) {
        FLFidel.deleteInstructions = [self getStringValueFor:kDeleteInstructionsOptionKey fromDictionary:options];
    }
    
    if([self valueIsValidFor:kPrivacyURLOptionKey fromDictionary:options]) {
        FLFidel.privacyURL = [self getStringValueFor:kPrivacyURLOptionKey fromDictionary:options];
    }
    
    if([self valueIsValidFor:kTermsConditionsURLOptionKey fromDictionary:options]) {
        FLFidel.termsConditionsURL = [self getStringValueFor:kTermsConditionsURLOptionKey fromDictionary:options];
    }
}

- (NSString * _Nullable)getStringValueFor:(NSString *)key fromDictionary:(NSDictionary *)dict {
    NSArray *allKeys = dict.allKeys;
    if ([allKeys containsObject:key]) {
        id value = dict[key];
        if ([value isKindOfClass:[NSString class]]) {
            return value;
        }
        else {
            return [value stringValue];
        }
    }
    return nil;
}

- (BOOL)valueIsValidFor:(NSString *)key fromDictionary:(NSDictionary *)dict {
    NSArray *allKeys = dict.allKeys;
    id value = dict[key];
    if ([allKeys containsObject:key]) {
        if ([value isKindOfClass:[NSString class]]) {
            return [allKeys containsObject:key] && (value != nil) && ![value isKindOfClass:[NSNull class]] && [value length] > 0;
        }
        return [allKeys containsObject:key] && (value != nil) && ![value isKindOfClass:[NSNull class]];
    }
    return false;
}

@end
